package gazpacho.core.stream.plex.model;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.stream.plex.model.io.Metadata;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Show(@NonNull String ratingKey,
                   @NonNull String title,
                   @NonNull String librarySectionId,
                   Integer year,
                   Integer seasons) {

    public static Show fromMetadata(@NonNull Metadata metadata) {
        if (!MediaReleaseType.TV_SHOW.equals(metadata.type())) {
            throw new IllegalArgumentException(
                    String.format("Invalid metadata media type %s", metadata.type()));
        }
        return Show.builder()
                .ratingKey(metadata.ratingKey())
                .title(metadata.title())
                .librarySectionId(String.valueOf(metadata.librarySectionID()))
                .year(metadata.year())
                .seasons(metadata.childCount())
                .build();
    }
}
