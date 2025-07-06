package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.MediaDataSource;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaDataSourceRepository extends JpaRepository<MediaDataSource, Long> {

    @EntityGraph(value = MediaDataSource.WITH_REQUEST_RELEASE_PROFILE_GRAPH)
    Optional<MediaDataSource> findWithRequestGraphById(Long id);
}