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