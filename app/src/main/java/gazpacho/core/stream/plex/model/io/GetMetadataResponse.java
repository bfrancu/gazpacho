package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record GetMetadataResponse(@JsonProperty("MediaContainer")
                                  MediaContainer mediaContainer) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder(toBuilder = true)
    public record MediaContainer(Integer size,
                                 Boolean allowSync,
                                 String identifier,
                                 Integer librarySectionID,
                                 String librarySectionTitle,
                                 String librarySectionUUID,
                                 String mediaTagPrefix,
                                 Long mediaTagVersion,
                                 @JsonProperty("Metadata")
                                 List<Metadata> metadata) {}
}
