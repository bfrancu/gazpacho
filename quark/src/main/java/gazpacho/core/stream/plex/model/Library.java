package gazpacho.core.stream.plex.model;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.stream.plex.model.io.Directory;
import gazpacho.core.stream.plex.model.io.Location;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record Library(@NonNull MediaReleaseType mediaReleaseType,
                      @NonNull String sectionId,
                      @NonNull String title,
                      @NonNull Location location) {

    public static Library fromDirectory(@NonNull Directory directory) {
        Location location = directory.locations().isEmpty()
                ? Location.builder().build()
                : directory.locations().getFirst();

        return Library.builder()
                .mediaReleaseType(directory.type())
                .sectionId(directory.key())
                .title(directory.title())
                .location(location)
                .build();
    }
}
