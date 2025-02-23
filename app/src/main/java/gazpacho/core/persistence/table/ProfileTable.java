package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.Profile_;
import gazpacho.core.persistence.query.ProfileQueries;
import gazpacho.core.persistence.query.ProfileQueries_;
import lombok.NonNull;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class ProfileTable extends EntityCrudBaseTable<String, ProfileQueries, Profile> {
    public ProfileTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Profile> getProfileByPhoneNumber(@NonNull String phoneNumber) {
        return sessionFactory.fromTransaction(session -> {
            session.enableFetchProfile(Profile_.PROFILE_WITH_REQUESTS_WISHLIST);
            return get(phoneNumber, session);
        });
    }

    @Override
    protected Profile retrieve(ProfileQueries q, String phoneNumber) throws ObjectNotFoundException {
        return q.getProfile(phoneNumber);
    }

    @Override
    protected ProfileQueries queries(Session session) {
        return new ProfileQueries_(session);
    }
}
/*
@RequiredArgsConstructor
@Slf4j
public class ProfileTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileTable.class);
    private final SessionFactory sessionFactory;

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
        sessionFactory.inTransaction(session -> session.persist(profile));
    }

    public void addWish(@NonNull String phoneNumber, @NonNull Wish wish) {
        update(phoneNumber, ((profile, session) -> {
            wish.setProfile(profile);
            session.persist(wish);
        }));
    }

    public void removeWish(@NonNull String phoneNumber, @NonNull String wishId) {
        update(phoneNumber, ((profile, session) -> {
            profile.setWishlist(profile.getWishlist()
                    .stream()
                    .filter(wish -> !wishId.equals(wish.getId()))
                    .collect(Collectors.toSet()));
            session.persist(profile);
        }));
    }

    public void update(@NonNull String phoneNumber, Consumer<Profile> updater) {
        update(phoneNumber, (profile, session) -> {
            updater.accept(profile);
            session.persist(profile);
        });
    }

    public void remove(@NonNull String phoneNumber) {
        update(phoneNumber, ((profile, session) -> session.remove(profile)));
    }

    private void update(String phoneNumber, BiConsumer<Profile, Session> updater) {
        sessionFactory.inTransaction(session -> {
            Optional<Profile> persistedProfile = get(phoneNumber, session);
            persistedProfile.ifPresent(profile -> updater.accept(profile, session));
        });
    }

    private Optional<Profile> get(String phoneNumber, Session session) {
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
*/