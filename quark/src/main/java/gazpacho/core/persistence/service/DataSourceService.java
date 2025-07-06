package gazpacho.core.persistence.service;

import gazpacho.core.model.DataSource;
import gazpacho.core.persistence.model.MediaDataSource;
import gazpacho.core.persistence.model.mapper.DataSourceMapper;
import gazpacho.core.persistence.repository.MediaDataSourceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class DataSourceService {
    @Inject
    private MediaDataSourceRepository mediaDataSourceRepository;

    @Inject
    private DataSourceMapper dataSourceMapper;

    @Transactional
    public DataSource persist(DataSource dataSource) {
        return fromPersistence(mediaDataSourceRepository.save(toPersistence(dataSource)));
    }

    private MediaDataSource toPersistence(DataSource dataSource) {
        return dataSourceMapper.toPersistenceModel(dataSource);
    }

    private DataSource fromPersistence(MediaDataSource mediaDataSource) {
        return dataSourceMapper.fromPersistenceModel(mediaDataSource);
    }
}