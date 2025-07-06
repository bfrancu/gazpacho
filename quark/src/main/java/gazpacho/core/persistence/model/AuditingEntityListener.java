package gazpacho.core.persistence.model;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditingEntityListener {
    @PrePersist
    void preCreate(Versioned versioned) {
        LocalDateTime now = LocalDateTime.now();
        versioned.setCreatedDate(now);
        versioned.setLastUpdated(now);
    }

    @PreUpdate
    void preUpdate(Versioned versioned) {
        versioned.setLastUpdated(LocalDateTime.now());
    }
}
