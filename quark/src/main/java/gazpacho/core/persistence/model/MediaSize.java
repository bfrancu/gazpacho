package gazpacho.core.persistence.model;

import gazpacho.core.model.SizeUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Basic;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Builder;

@Embeddable
@Builder(toBuilder = true)
public record MediaSize(
        @Enumerated(EnumType.STRING)
        @Basic(optional = false)
        @Column(name = "media_size_unit")
        SizeUnit unit,
        @Basic(optional = false)
        @Column(name = "media_size")
        Double size) {}
