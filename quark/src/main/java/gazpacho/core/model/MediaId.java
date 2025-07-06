package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record MediaId(@NonNull Long tmdbId,
                      @NonNull MediaType mediaType) {
}
