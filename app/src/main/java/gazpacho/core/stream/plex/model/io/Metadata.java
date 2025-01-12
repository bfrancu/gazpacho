package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gazpacho.core.model.MediaType;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Metadata(String ratingKey,
                       String key,
                       String guid,
                       String slug,
                       String studio,
                       MediaType type,
                       String title,
                       String contentRating,
                       String summary,
                       Double rating,
                       Double audienceRating,
                       Integer year,
                       String tagline,
                       String art,
                       Long duration,
                       String originallyAvailableAt,
                       Long addedAt,
                       Long updatedAt,
                       String primaryExtraKey,
                       String ratingImage,
                       String titleSort,
                       Integer viewCount,
                       Long lastViewedAt,
                       String originalTitle,
                       Long viewOffset,
                       Integer skipCount,
                       Integer index,
                       Integer leafCount,
                       Integer viewedLeafCount,
                       Integer childCount,
                       String parentRatingKey,
                       String parentGuid,
                       String parentKey,
                       String parentSlug,
                       String parentStudio,
                       String parentTitle,
                       Integer parentIndex,
                       String librarySectionTitle,
                       Integer librarySectionID,
                       String librarySectionKey,
                       @JsonProperty("Media")
                       List<Media> media,
                       @JsonProperty("Genre")
                       List<GenericMetadata> genres,
                       @JsonProperty("Country")
                       List<GenericMetadata> countries,
                       @JsonProperty("Director")
                       List<GenericMetadata> directors,
                       @JsonProperty("Writer")
                       List<GenericMetadata> writers,
                       @JsonProperty("Role")
                       List<GenericMetadata> roles,
                       @JsonProperty("Producer")
                       List<GenericMetadata> producers,
                       @JsonProperty("Image")
                       List<Image> images,
                       @JsonProperty("Guid")
                       List<Guid> guids) {}
