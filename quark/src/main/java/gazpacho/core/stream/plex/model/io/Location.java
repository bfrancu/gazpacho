package gazpacho.core.stream.plex.model.io;

import lombok.Builder;

@Builder
public record Location(String id,
                       String path) {
}
