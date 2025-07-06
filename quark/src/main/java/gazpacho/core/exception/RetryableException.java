package gazpacho.core.exception;

import lombok.NonNull;

public class RetryableException extends RuntimeException {

    public RetryableException(@NonNull Exception e) {
        super(e);
    }

    public RetryableException(@NonNull String message) {
        super(message);
    }

    public RetryableException(@NonNull String message, @NonNull Exception e) {
        super(message, e);
    }
}
