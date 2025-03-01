package gazpacho.core.match;

import gazpacho.core.model.VisualMedia;
import lombok.NonNull;

@FunctionalInterface
public interface MediaMatcher {
    boolean match(@NonNull VisualMedia visualMedia);
}
