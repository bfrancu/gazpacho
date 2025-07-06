package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record SearchResponse(@JsonProperty("MediaContainer")
                             MediaContainer mediaContainer) {

    public record MediaContainer(Integer size,
                                 Boolean allowSync,
                                 String art,
                                 String identifier,
                                 String mediaTagPrefix,
                                 Long mediaTagVersion,
                                 Boolean mixedParents,
                                 Boolean nocache,
                                 String thumb,
                                 String title1,
                                 String title2,
                                 String viewGroup,
                                 Integer viewMode,
                                 @JsonProperty("Metadata")
                                 List<Metadata> metadata) {}
}
