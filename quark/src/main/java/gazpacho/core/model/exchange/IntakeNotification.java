package gazpacho.core.model.exchange;

import gazpacho.core.model.UserProfile;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(fluent = true)
@SuperBuilder
@RequiredArgsConstructor
public class IntakeNotification<Payload> {
    @NonNull private final UserProfile userProfile;
    @NonNull private final Payload payload;
}

