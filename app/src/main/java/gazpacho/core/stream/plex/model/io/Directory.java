package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gazpacho.core.model.MediaType;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Directory(Boolean directory,
                        MediaType type,
                        Long updatedAt,
                        Long createdAt,
                        Long contentChangedAt,
                        Integer leafCount,
                        String key,
                        String title,
                        String language,
                        String uuid,
                        @JsonProperty("Location")
                        List<Location> locations) {}
