package gazpacho.core.stream.plex.model;

import gazpacho.core.model.MediaType;
import gazpacho.core.stream.plex.model.io.Metadata;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Season(@NonNull String ratingKey,
                     @NonNull String title,
                     @NonNull String parentRatingKey,
                     Integer season,
                     Integer episodes) {

    public static Season fromMetadata(@NonNull Metadata metadata) {
        if (!MediaType.TV_SEASON.equals(metadata.type())) {
            throw new IllegalArgumentException(
                    String.format("Invalid metadata media type %s", metadata.type()));
        }

        return Season.builder()
                .ratingKey(metadata.ratingKey())
                .title(metadata.title())
                .parentRatingKey(metadata.parentRatingKey())
                .season(metadata.index())
                .episodes(metadata.leafCount())
                .build();
    }
}
