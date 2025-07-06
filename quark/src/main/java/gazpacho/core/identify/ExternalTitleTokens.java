package gazpacho.core.identify;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ExternalTitleTokens(@NonNull String titleId,
                                  Integer season) {}
