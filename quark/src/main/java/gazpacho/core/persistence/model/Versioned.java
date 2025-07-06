package gazpacho.core.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Versioned {
    @Version
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
