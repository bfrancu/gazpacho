package gazpacho.core.identify;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MediaItemQueryTokens(
        @NonNull String name,
        Integer releaseYear,
        Integer season,
        Integer episode) {
}


