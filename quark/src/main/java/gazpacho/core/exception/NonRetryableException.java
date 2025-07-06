package gazpacho.core.exception;

import lombok.NonNull;

public class NonRetryableException extends RuntimeException {

    public NonRetryableException(@NonNull Exception e) {
        super(e);
    }

    public NonRetryableException(@NonNull String message) {
        super(message);
    }

    public NonRetryableException(@NonNull String message, @NonNull Exception e) {
        super(message, e);
    }
}
