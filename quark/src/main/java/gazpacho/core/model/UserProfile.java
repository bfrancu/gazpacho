package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserProfile(@NonNull String id,
                          String firstName,
                          String lastName) {
}