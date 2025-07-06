package gazpacho.core.identify;

import lombok.NonNull;

import java.util.Optional;

public interface QueryTokensParser {
    Optional<MediaItemQueryTokens> parse(@NonNull String requestPayload);
}
