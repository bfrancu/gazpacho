package gazpacho.core.stream.plex.model.io;

import lombok.Builder;

@Builder(toBuilder = true)
public record Rating(String image,
                     Double value,
                     String type) {
}
