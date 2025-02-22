package gazpacho.core.stream.plex.model;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.stream.plex.model.io.Metadata;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Episode(@NonNull String ratingKey,
                      @NonNull String parentRatingKey,
                      @NonNull String title,
                      Integer episode,
                      Integer season) {

    public static Episode fromMetadata(@NonNull Metadata metadata) {
        if (!MediaReleaseType.TV_EPISODE.equals(metadata.type())) {
            throw new IllegalArgumentException(
                    String.format("Invalid metadata media type %s", metadata.type()));
        }

        return Episode.builder()
                .ratingKey(metadata.ratingKey())
                .title(metadata.title())
                .parentRatingKey(metadata.parentRatingKey())
                .season(metadata.parentIndex())
                .episode(metadata.index())
                .build();
    }
}
