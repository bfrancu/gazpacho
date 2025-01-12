package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gazpacho.core.model.MediaType;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record GetLibraryItemsResponse(@JsonProperty("MediaContainer")
                                      MediaContainer mediaContainer) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder(toBuilder = true)
    public record MediaContainer(Integer size,
                                 Boolean allowSync,
                                 String art,
                                 String content,
                                 String identifier,
                                 String librarySectionID,
                                 String librarySectionTitle,
                                 String librarySectionUUID,
                                 String mediaTagPrefix,
                                 String mediaTagVersion,
                                 String thumb,
                                 String title1,
                                 String title2,
                                 MediaType viewGroup,
                                 Integer viewMode,
                                 Boolean mixedParents,
                                 Boolean nocache,
                                 @JsonProperty("Metadata")
                                 List<Metadata> metadata) {}
}
