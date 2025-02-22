package gazpacho.core.persistence.model;

import gazpacho.core.model.SizeUnit;
import jakarta.persistence.*;

@Embeddable
public record MediaSize(
        @Enumerated(EnumType.STRING)
        @Basic(optional = false)
        @Column(name = "media_size_unit")
        SizeUnit unit,
        @Basic(optional = false)
        @Column(name = "media_size")
        Double size) {}

