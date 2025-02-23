package gazpacho.core.persistence.query;

import gazpacho.core.persistence.model.MediaDataSource;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.CheckHQL;
import org.hibernate.annotations.processing.HQL;

import java.util.List;

@CheckHQL
public interface MediaDataSourceQueries {
    EntityManager entityManager();

    @HQL("select dataSource from MediaDataSource as dataSource " +
            "join fetch dataSource.request " +
            "where dataSource.torrentId like :torrentId")
    List<MediaDataSource> findDataSourceByTorrentId(String torrentId);
}
