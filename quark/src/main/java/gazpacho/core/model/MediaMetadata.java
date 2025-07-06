package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MediaMetadata(
        @NonNull String title,
        @NonNull String description,
        @NonNull String language,
        @NonNull LocalDate firstAirDate,
        MediaId mediaId,
        Double popularity,
        String originCountry,
        Integer seasonsCount) {

    public MediaType mediaType() {
        return mediaId.mediaType();
    }

    public Long tmdbId() {
        return mediaId.tmdbId();
    }
}
