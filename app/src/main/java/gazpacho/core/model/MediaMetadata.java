package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MediaMetadata(
        @NonNull Long tmdbId,
        @NonNull MediaType mediaType,
        @NonNull String title,
        @NonNull String description,
        @NonNull String language,
        @NonNull LocalDate firstAirDate,
        @NonNull Double popularity,
        String originCountry,
        Integer seasonsCount) {
}
