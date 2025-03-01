package gazpacho.core.persistence.query;

import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.CheckHQL;
import org.hibernate.annotations.processing.Find;

@CheckHQL
public interface MediaItemQueries {
    EntityManager entityManager();

    @Find
    MediaItem getMediaItem(MediaItemId mediaItemId);
}
