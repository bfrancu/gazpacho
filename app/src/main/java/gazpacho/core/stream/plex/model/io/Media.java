package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record Media(String id,
                    Long duration,
                    Integer bitrate,
                    Integer width,
                    Integer height,
                    Double aspectRatio,
                    Integer audioChannels,
                    String audioCodec,
                    String videoCodec,
                    String videoResolution,
                    String container,
                    String videoFrameRate,
                    Integer optimizedForStreaming,
                    String audioProfile,
                    Boolean has64bitOffsets,
                    String videoProfile,
                    @JsonProperty("Part")
                    List<Part> parts) {
}
