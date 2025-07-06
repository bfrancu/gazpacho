package gazpacho.core.stream.plex.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record Stream(String id,
                     Integer streamType,
                     @JsonProperty("default")
                     Boolean isDefault,
                     String codec,
                     Integer index,
                     Integer bitrate,
                     Integer bitDepth,
                     String chromaLocation,
                     Integer chromaSubsampling,
                     Integer codedHeight,
                     Integer codedWidth,
                     String colorPrimaries,
                     String colorRange,
                     String colorSpace,
                     String colorTrc,
                     Integer frameRate,
                     Boolean hasScalingMatrix,
                     Integer height,
                     Integer level,
                     String profile,
                     Integer refFrames,
                     String scanType,
                     String streamIdentifier,
                     Integer width,
                     String displayTitle,
                     String extendedDisplayTitle,
                     Boolean selected,
                     Integer channels,
                     String language,
                     String languageTag,
                     String languageCode,
                     Integer samplingRate) {
}
