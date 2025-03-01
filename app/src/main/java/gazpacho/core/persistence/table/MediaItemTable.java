package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.query.MediaItemQueries;
import gazpacho.core.persistence.query.MediaItemQueries_;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Optional;

public class MediaItemTable extends EntityCrudBaseTable<MediaItemId, MediaItemQueries, MediaItem> {

    public MediaItemTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<MediaItem> getMediaItemByTmdbId(MediaItemId mediaItemId) {
        return sessionFactory.fromTransaction(session -> get(mediaItemId, session));
    }

    @Override
    protected MediaItem retrieve(MediaItemQueries q, MediaItemId mediaItemId) throws ObjectNotFoundException {
        return q.getMediaItem(mediaItemId);
    }

    @Override
    protected MediaItemQueries queries(Session session) {
        return new MediaItemQueries_(session);
    }
}
