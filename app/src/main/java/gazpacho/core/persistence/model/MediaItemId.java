package gazpacho.core.persistence.model;

import gazpacho.core.model.MediaType;
import jakarta.persistence.*;
import lombok.NonNull;

@Embeddable
public record MediaItemId(@NonNull
                          @Column(name = "tmdb_id")
                          Long tmdbId,
                          @NonNull
                          @Enumerated(EnumType.STRING)
                          @Column(name ="media_type")
                          MediaType mediaType) {
}
