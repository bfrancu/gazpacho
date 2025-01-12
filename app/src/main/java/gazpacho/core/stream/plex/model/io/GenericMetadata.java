package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public record GenericMetadata(
        String id,
        String filter,
        String tag,
        String tagKey,
        String role,
        String thumb) {}
