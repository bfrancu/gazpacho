package gazpacho.core.stream.plex.model;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.stream.plex.model.io.Metadata;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Movie(@NonNull String ratingKey,
                    @NonNull String title,
                    Integer year) {

    public static Movie fromMetadata(@NonNull Metadata metadata) {
        if (!MediaReleaseType.MOVIE.equals(metadata.type())) {
            throw new IllegalArgumentException(
                    String.format("Invalid metadata media type %s", metadata.type()));
        }

        return Movie.builder()
                .ratingKey(metadata.ratingKey())
                .title(metadata.title())
                .year(metadata.year())
                .build();
    }
}
