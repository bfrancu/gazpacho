package gazpacho.core.stream.plex.model.io;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record GetAllLibrariesResponse(@JsonProperty("MediaContainer")
                                      MediaContainer mediaContainer) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder(toBuilder = true)
    public record MediaContainer(Integer size,
                                 Boolean allowSync,
                                 String title1,
                                 @JsonProperty("Directory")
                                 List<Directory> directories) {
    }
}
