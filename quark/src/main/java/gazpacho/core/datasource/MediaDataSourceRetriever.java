package gazpacho.core.datasource;

import gazpacho.core.model.DataSource;
import gazpacho.core.model.VisualMedia;
import lombok.NonNull;

import java.util.Optional;

/**
 * Searches for a media item and retrieves a path to a local data source corresponding to the media item
 */
public interface MediaDataSourceRetriever {
    Optional<DataSource> retrieveDataSource(@NonNull VisualMedia visualMedia);
}
