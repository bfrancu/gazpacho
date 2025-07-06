package gazpacho.core.model.exchange;

import lombok.Builder;

@Builder
public record DownloadNotificationResponse(NotificationRequestStatus status,
                                           RequestFailure requestFailure,
                                           String message) {
}
