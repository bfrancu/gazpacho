package gazpacho.core.model.dto;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.MediaType;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.persistence.model.MediaItemId;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record MediaReleaseDto(
        Long releaseId,
        Long tmdbId,
        MediaType mediaType,
        MediaReleaseType mediaReleaseType,
        Integer season,
        Integer episode,
        Boolean inLibrary) {

    public static MediaReleaseDto fromVisualMedia(@NonNull VisualMedia visualMedia) {
        return MediaReleaseDto.builder()
                .tmdbId(visualMedia.metadata().tmdbId())
                .mediaType(visualMedia.metadata().mediaType())
                .mediaReleaseType(visualMedia.release().mediaReleaseType())
                .season(visualMedia.release().season())
                .episode(visualMedia.release().episode())
                .build();
    }

    public MediaItemId getMediaItemId() {
        return MediaItemId.builder()
                .tmdbId(tmdbId)
                .mediaType(mediaType)
                .build();
    }
}
