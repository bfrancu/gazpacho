package gazpacho.core.model;

import gazpacho.core.datasource.filelist.model.DownloadSize;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record DataSource(@NonNull String name,
                         @NonNull DownloadSize size,
                         @NonNull String location,
                         @NonNull Boolean finished,
                         String torrentHash,
                         Long persistenceId,
                         UserRequest userRequest) {
    public static class DataSourceBuilder {
        Long persistenceId = 0L;
    }

    public boolean hasPersistenceId() {
        return persistenceId != null && persistenceId > 0;
    }
}
