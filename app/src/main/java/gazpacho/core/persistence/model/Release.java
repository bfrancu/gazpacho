package gazpacho.core.persistence.model;


import gazpacho.core.model.MediaReleaseType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "\"Releases\"")
public class Release extends Versioned {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "release_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tmdb_id"),
            @JoinColumn(name = "media_type")})
    private MediaItem item;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = Request_.RELEASE,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Request request;

    @Basic(optional = false)
    private LocalDateTime created;

    @Basic(optional = false)
    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Basic(optional = false)
    @Column(name = "release_type")
    @Enumerated(EnumType.STRING)
    private MediaReleaseType releaseType;

    private Integer season;

    private Integer episode;
}
