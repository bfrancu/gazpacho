package gazpacho.core.datasource.filelist.model;

import gazpacho.core.model.VideoQuality;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record SearchResultEntry(@NonNull CategoryDetails category,
                                @NonNull String title,
                                @NonNull String detailsLink,
                                @NonNull String downloadLink,
                                @NonNull DownloadSize downloadSize,
                                @NonNull VideoQuality videoQuality,
                               Integer seeders) {
}