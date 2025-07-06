package gazpacho.core.model.dto;

import gazpacho.core.model.MediaMetadata;
import gazpacho.core.model.MediaType;
import gazpacho.core.persistence.model.MediaItemId;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MediaItemDto(Long tmdbId,
                           MediaType mediaType,
                           String title,
                           String description,
                           Integer seasonsCount,
                           LocalDate firstAirDate,
                           LocalDate lastAirDate,
                           LocalDate nextAirDate) {

    public static MediaItemDto fromMetadata(@NonNull MediaMetadata metadata) {
        return MediaItemDto.builder()
                .tmdbId(metadata.tmdbId())
                .mediaType(metadata.mediaType())
                .title(metadata.title())
                .description(metadata.description())
                .seasonsCount(metadata.seasonsCount())
                .firstAirDate(metadata.firstAirDate())
                .build();
    }

    public MediaItemId getMediaItemId() {
        return MediaItemId.builder()
                .tmdbId(tmdbId)
                .mediaType(mediaType)
                .build();
    }
}
