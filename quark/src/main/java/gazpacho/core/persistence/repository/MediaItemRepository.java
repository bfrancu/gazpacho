package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaItemRepository extends JpaRepository<MediaItem, MediaItemId> {

    Optional<MediaItem> getMediaItemById(MediaItemId id);

    @EntityGraph(value = MediaItem.WITH_RELEASES_GRAPH)
    Optional<MediaItem> getMediaItemWithReleasesById(MediaItemId id);
}
