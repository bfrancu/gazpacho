package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record Part(String id,
                   String key,
                   Long duration,
                   String file,
                   Long size,
                   String audioProfile,
                   String container,
                   Boolean has64bitOffsets,
                   Boolean optimizedForStreaming,
                   String videoProfile,
                   @JsonProperty("Stream")
                   List<Stream> streams) {
}
