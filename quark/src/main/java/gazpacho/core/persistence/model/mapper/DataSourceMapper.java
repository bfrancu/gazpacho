package gazpacho.core.persistence.model.mapper;

import gazpacho.core.datasource.filelist.model.DownloadSize;
import gazpacho.core.model.DataSource;
import gazpacho.core.persistence.model.MediaDataSource;
import gazpacho.core.persistence.model.MediaSize;
import gazpacho.core.persistence.service.EntityFetcherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class DataSourceMapper implements PersistenceModelMapper<DataSource, MediaDataSource> {
    @Inject
    EntityFetcherService entityFetcherService;

    @Inject
    RequestModelMapper requestModelMapper;

    @Override
    public MediaDataSource toPersistenceModel(DataSource domainModel) {
        MediaDataSource.MediaDataSourceBuilder dataSourceBuilder = MediaDataSource.builder();

        if (domainModel.hasPersistenceId()) {
            dataSourceBuilder = entityFetcherService.fetchMediaDataSource(domainModel.persistenceId())
                    .toBuilder();
        }

        dataSourceBuilder = dataSourceBuilder.torrentName(domainModel.name())
                .downloadLocation(domainModel.location())
                .finished(domainModel.finished())
                .mediaSize(MediaSize.builder()
                        .size(domainModel.size().size())
                        .unit(domainModel.size().unit())
                        .build());

        if (!StringUtils.isBlank(domainModel.torrentHash())) {
            dataSourceBuilder.torrentHash(domainModel.torrentHash());
        }

        if (null != domainModel.userRequest()) {
            dataSourceBuilder.request(entityFetcherService.fetchRequest(domainModel.userRequest().persistenceId()));
        }

        return dataSourceBuilder.build();
    }

    @Override
    public DataSource fromPersistenceModel(MediaDataSource persistenceModel) {
        DataSource.DataSourceBuilder dataSourceBuilder = DataSource.builder()
                .name(persistenceModel.getTorrentName())
                .location(persistenceModel.getDownloadLocation())
                .finished(persistenceModel.getFinished())
                .size(DownloadSize.builder()
                        .size(persistenceModel.getMediaSize().size())
                        .unit(persistenceModel.getMediaSize().unit())
                        .build())
                .persistenceId(persistenceModel.getId());

        if (!StringUtils.isBlank(persistenceModel.getTorrentHash())) {
            dataSourceBuilder.torrentHash(persistenceModel.getTorrentHash());
        }

        if (null != persistenceModel.getRequest()) {
            dataSourceBuilder.userRequest(requestModelMapper.fromPersistenceModel(persistenceModel.getRequest()));
        }

        return dataSourceBuilder.build();
    }
}
