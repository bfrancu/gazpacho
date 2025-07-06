package gazpacho.core.persistence.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;


@NamedEntityGraph(
        name = MediaItem.WITH_RELEASES_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("releases")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name="\"MediaItems\"")
public class MediaItem extends Versioned {
    public static final String WITH_RELEASES_GRAPH = "media-item-with-releases";

    @EmbeddedId
    private MediaItemId id;

    public MediaItem(MediaItemId mediaItemId) {
        this.id = mediaItemId ;
    }

    @ToString.Exclude
    @OneToOne(mappedBy = MediaItemSubscription.ITEM_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private MediaItemSubscription subscription;

    @ToString.Exclude
    @OneToMany(mappedBy = Request.ITEM_FIELD)
    private Set<Request> requests;

    @ToString.Exclude
    @OneToMany(mappedBy = Release.ITEM_FIELD,
            fetch = FetchType.LAZY)
    private Set<Release> releases;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(
            name = "next_release",
            referencedColumnName = "release_id")
    private Release nextRelease;

    @Basic(optional = false)
    private String title;

    @Basic(optional = false)
    private String description;

    @Basic(optional = false)
    private String language;

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
