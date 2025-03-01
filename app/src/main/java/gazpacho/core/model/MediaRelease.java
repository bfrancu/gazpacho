package gazpacho.core.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MediaRelease(LocalDate releaseDate,
                           MediaReleaseType mediaReleaseType,
                           Integer season,
                           Integer episode) {
}
