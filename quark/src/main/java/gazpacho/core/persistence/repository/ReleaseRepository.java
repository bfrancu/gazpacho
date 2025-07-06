package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.Release;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @EntityGraph(value = Release.WITH_MEDIA_ITEM_GRAPH)
    Optional<Release> getReleaseById(Long id);
}
