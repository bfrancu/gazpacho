package gazpacho.core.download.filelist.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DownloadSize(@NonNull Double size,
                           @NonNull SizeUnit unit) {}