package gazpacho.core.model;

public enum RequestStatus {
    PENDING,
    MEDIA_MATCHED,
    PENDING_USER_CONFIRMATION,
    USER_CONFIRMED,
    QUEUED_FOR_DOWNLOAD,
    COMPLETED,
    CANCELLED
}
