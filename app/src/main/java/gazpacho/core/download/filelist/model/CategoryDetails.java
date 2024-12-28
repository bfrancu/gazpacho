package gazpacho.core.download.filelist.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CategoryDetails(@NonNull String label,
                              @NonNull MediaCategory category) {}
