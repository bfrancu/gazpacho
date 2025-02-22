package gazpacho.core.persistence.table;

import gazpacho.DemoJdbc;
import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.Profile_;
import gazpacho.core.persistence.model.Wish;
import gazpacho.core.persistence.query.ProfileQueries;
import gazpacho.core.persistence.query.ProfileQueries_;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class ProfileTable {
    private final SessionFactory sessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileTable.class);

    public Optional<Profile> getProfileByPhoneNumber(@NonNull String phoneNumber) {
        return sessionFactory.fromTransaction(session -> {
            session.enableFetchProfile(Profile_.PROFILE_WITH_REQUESTS_WISHLIST);
            return get(phoneNumber, session);
        });
    }

    public boolean exists(@NonNull String phoneNumber) {
        return sessionFactory.fromTransaction(session -> get(phoneNumber, session).isPresent());
    }

    public void persist(@NonNull Profile profile) {
        sessionFactory.inTransaction(session -> {
            session.persist(profile);
        });
    }

    public void addWish(@NonNull String phoneNumber, @NonNull Wish wish) {
        update(phoneNumber, ((profile, session) -> {
            wish.setProfile(profile);
            session.persist(wish);
        }));
    }

    public void update(@NonNull String phoneNumber, Consumer<Profile> updater) {
        update(phoneNumber, ((profile, session) -> {
            updater.accept(profile);
            session.persist(profile);
        }));
    }

    private void update(@NonNull String phoneNumber, BiConsumer<Profile, Session> updater) {
        sessionFactory.inTransaction(session -> {
            Optional<Profile> persistedProfile = get(phoneNumber, session);
            persistedProfile.ifPresent(profile -> updater.accept(profile, session));
        });
    }

    private Optional<Profile> get(@NonNull String phoneNumber, Session session) {
        try {
            Profile persistedProfile = queries(session).getProfile(phoneNumber);
            LOGGER.info("Object found {}", persistedProfile);
            return Optional.of(persistedProfile);
        } catch (ObjectNotFoundException e) {
            LOGGER.info("Object not found {}", e);
            return Optional.empty();
        }
    }

    private ProfileQueries queries(Session session) {
        return new ProfileQueries_(session);
    }
}