package gazpacho.core.persistence.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="\"MediaItems\"")
public class MediaItem extends Versioned {
    @Id
    @NonNull
    @Column(name = "tmdb_id")
    private Long tmdbId;

    public MediaItem(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "media_type")
    private MediaType mediaType;

    @ToString.Exclude
    @OneToOne(mappedBy = MediaItemSubscription_.ITEM,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private MediaItemSubscription subscription;

    @ToString.Exclude
    @OneToMany(mappedBy = Request_.ITEM)
    private Set<Request> requests;

    @Basic(optional=false)
    private String title;

    @Basic(optional=false)
    private String description;

    @Column(name = "season_count")
    private Integer seasonCount;

    @Basic(optional = false)
    @Column(name = "first_air_date")
    private LocalDate firstAirDate;

    @Column(name = "last_air_date")
    private LocalDate lastAirDate;

    @Column(name = "next_air_date")
    private LocalDate nextAirDate;
}

