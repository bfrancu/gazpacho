package gazpacho.core.persistence.table;

import gazpacho.core.persistence.model.MediaDataSource;
import gazpacho.core.persistence.query.MediaDataSourceQueries;
import gazpacho.core.persistence.query.MediaDataSourceQueries_;
import lombok.NonNull;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class MediaDataSourceTable extends EntityCrudBaseTable<String, MediaDataSourceQueries, MediaDataSource> {

    public MediaDataSourceTable(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<MediaDataSource> getDataSourceByTorrentId(@NonNull String torrentId) {
        return sessionFactory.fromTransaction(session -> get(torrentId, session));
    }

    @Override
    protected MediaDataSource retrieve(MediaDataSourceQueries q, String torrentId) throws ObjectNotFoundException {
        List<MediaDataSource> dataSources = q.findDataSourceByTorrentId(torrentId);
        if (dataSources.isEmpty()) {
            LOGGER.warn("No media data source found for torrent id {}", torrentId);
            throw new ObjectNotFoundException(Optional.ofNullable(torrentId), "MediaDataSource");
        }
        return dataSources.getFirst();
    }

    @Override
    protected MediaDataSourceQueries queries(Session session) {
        return new MediaDataSourceQueries_(session);
    }
}
