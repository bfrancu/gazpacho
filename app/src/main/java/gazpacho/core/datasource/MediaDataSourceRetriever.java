package gazpacho.core.datasource;

import gazpacho.core.model.MediaItem;
import lombok.NonNull;

import java.nio.file.Path;

/**
 * Searches for a media item and retrieves a path to a local data source corresponding to the media item
 */
public interface MediaDataSourceRetriever {
    Path retrieveDataSource(@NonNull MediaItem mediaItem);
}
