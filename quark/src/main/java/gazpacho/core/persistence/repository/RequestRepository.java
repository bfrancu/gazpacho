package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.Request;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @EntityGraph(value = Request.WITH_DATASOURCE_RELEASE_PROFILE_GRAPH)
    List<Request> findAllByQuery(String query);

    @EntityGraph(value = Request.WITH_DATASOURCE_RELEASE_PROFILE_GRAPH)
    Optional<Request> findById(Long id);
}