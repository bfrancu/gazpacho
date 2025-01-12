package gazpacho.core.match;

import gazpacho.core.model.MediaItem;
import lombok.NonNull;

@FunctionalInterface
public interface MediaMatcher {
    boolean match(@NonNull MediaItem mediaItem);
}
