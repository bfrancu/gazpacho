package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.MediaItemSubscription;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface MediaItemSubscriptionRepository extends JpaRepository<MediaItemSubscription, Long> {

    @Query("""
            select sub
            from MediaItemSubscription
            where date(sub.last_scanned) <= :sinceDate
            """
    )
    @QueryHints(
            @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "25")
    )
    @EntityGraph(value = MediaItemSubscription.WITH_ITEM_SUBSCRIBERS_GRAPH)
    Stream<MediaItemSubscription> streamByLastScannedSinceDate(@Param("sinceDate")LocalDate sinceDate);

    Optional<MediaItemSubscription> getSubscriptionById(Long id);
}