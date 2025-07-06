package gazpacho.core.datasource.transmission.model.io;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public enum Status {
    STOPPED(0),
    LOCAL_DATA_VERIFICATION_QUEUED(1),
    LOCAL_DATA_VERIFICATION(2),
    DOWNLOAD_QUEUED(3),
    DOWNLOADING(4),
    SEED_QUEUED(5),
    SEEDING(6);

    @Getter
    private final Integer value;

    Status(Integer value) {
        this.value = value;
    }
}
