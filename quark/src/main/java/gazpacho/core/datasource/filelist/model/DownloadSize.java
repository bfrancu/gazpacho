package gazpacho.core.datasource.filelist.model;

import gazpacho.core.model.SizeUnit;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record DownloadSize(@NonNull Double size,
                           @NonNull SizeUnit unit) {}