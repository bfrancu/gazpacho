package gazpacho.core.persistence.model;

import gazpacho.core.model.MediaId;
import gazpacho.core.model.MediaType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Builder;
import lombok.NonNull;

@Embeddable
@Builder(toBuilder = true)
public record MediaItemId(@NonNull
                          @Column(name = "tmdb_id")
                          Long tmdbId,
                          @NonNull
                          @Enumerated(EnumType.STRING)
                          @Column(name ="media_type")
                          MediaType mediaType) {

    public static MediaItemId fromMediaId(@NonNull MediaId mediaId) {
        return MediaItemId.builder()
                .tmdbId(mediaId.tmdbId())
                .mediaType(mediaId.mediaType())
                .build();
    }
}

