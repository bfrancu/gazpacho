package gazpacho.core.persistence.query;

import gazpacho.core.persistence.model.Profile;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.CheckHQL;
import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.query.Page;

import java.util.List;

@CheckHQL
public interface ProfileQueries {

    EntityManager entityManager();

    /*
    @HQL("select profile from Profile as profile " +
            "join fetch profile.requests " +
            "join fetch profile.wishlist " +
            "where profile.phoneNumber like :phoneNumber")
    */
    @HQL("where userId like :userId")
    List<Profile> findProfileByUserId(String userId, Page page);

    @Find
    Profile getProfile(String userId);
}
