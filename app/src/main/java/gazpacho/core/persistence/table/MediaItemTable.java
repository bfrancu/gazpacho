package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.query.MediaItemQueries;
import gazpacho.core.persistence.query.MediaItemQueries_;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Optional;

public class MediaItemTable extends EntityCrudBaseTable<Long, MediaItemQueries, MediaItem> {

    public MediaItemTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<MediaItem> getMediaItemByTmdbId(Long tmdbId) {
        return sessionFactory.fromTransaction(session -> get(tmdbId, session));
    }

    @Override
    protected MediaItem retrieve(MediaItemQueries q, Long tmdbId) throws ObjectNotFoundException {
        return q.getMediaItem(tmdbId);
    }

    @Override
    protected MediaItemQueries queries(Session session) {
        return new MediaItemQueries_(session);
    }
}
