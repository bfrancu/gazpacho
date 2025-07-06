package gazpacho.core.model.exchange;

import lombok.Builder;

@Builder
public record RequestFailure(String reason) {
}
