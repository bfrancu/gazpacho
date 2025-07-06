package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MediaRelease(LocalDate releaseDate,
                           @NonNull MediaReleaseType mediaReleaseType,
                           MediaId mediaId,
                           Integer season,
                           Integer episode,
                           boolean inLibrary,
                           Long persistenceId) {

    public static class MediaReleaseBuilder {
        boolean inLibrary = false;
        Long persistenceId = 0L;
    }

    public boolean hasPersistenceId() {
        return persistenceId != null && persistenceId > 0;
    }
}
