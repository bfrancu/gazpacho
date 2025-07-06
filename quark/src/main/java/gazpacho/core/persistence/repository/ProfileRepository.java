package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    @EntityGraph(value = Profile.WITH_REQUESTS_WISHLIST_GRAPH)
    Optional<Profile> getProfileByUserId(String userId);
}

