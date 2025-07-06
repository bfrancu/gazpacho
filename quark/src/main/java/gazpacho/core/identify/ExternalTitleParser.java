package gazpacho.core.identify;

import lombok.NonNull;

import java.util.Optional;

public interface ExternalTitleParser {
    Optional<ExternalTitleTokens> parse(@NonNull String queryString);
}
