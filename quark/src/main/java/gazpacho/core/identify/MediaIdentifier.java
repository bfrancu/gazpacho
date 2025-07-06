package gazpacho.core.identify;

import gazpacho.core.model.VisualMedia;
import lombok.NonNull;

import java.util.Optional;

public interface MediaIdentifier {
    /**
     * Identifies a media item by a passed string
     * @param identifier - string representation of the media item
     * @return - Optional<MediaItem> with value when identification is performed successfully
     */
    Optional<VisualMedia> identify(@NonNull String identifier);
}
