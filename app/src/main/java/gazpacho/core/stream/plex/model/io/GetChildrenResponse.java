package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record GetChildrenResponse(@JsonProperty("MediaContainer")
                                  MediaContainer mediaContainer) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder(toBuilder = true)
    public record MediaContainer(Integer size,
                                 Boolean allowSync,
                                 String art,
                                 String grandparentContentRating,
                                 Integer grandparentRatingKey,
                                 String grandparentTheme,
                                 String grandparentThumb,
                                 String grandparentTitle,
                                 String identifier,
                                 String key,
                                 Integer librarySectionID,
                                 String librarySectionTitle,
                                 String librarySectionUUID,
                                 String mediaTagPrefix,
                                 Long mediaTagVersion,
                                 Boolean nocache,
                                 Integer parentIndex,
                                 String parentTitle,
                                 Integer parentYear,
                                 String summary,
                                 String theme,
                                 String thumb,
                                 String title1,
                                 String title2,
                                 String viewGroup,
                                 Integer viewMode,
                                 @JsonProperty("Directory")
                                 List<Directory> directories,
                                 @JsonProperty("Metadata")
                                 List<Metadata> metadata) {}
}
