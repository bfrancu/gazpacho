package gazpacho.core.model.exchange;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class DownloadNotification extends IntakeNotification<DownloadNotification.Payload> {
    public record Payload(@NonNull String identifier) {}
}
