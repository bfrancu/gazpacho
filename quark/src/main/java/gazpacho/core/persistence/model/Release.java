package gazpacho.core.persistence.model;


import gazpacho.core.model.MediaReleaseType;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

import java.time.LocalDate;


@NamedEntityGraph(
        name = Release.WITH_MEDIA_ITEM_GRAPH,
        attributeNodes = {
                @NamedAttributeNode(Release.ITEM_FIELD)
        }
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "\"Releases\"")
public class Release extends Versioned {
    public static final String ITEM_FIELD = "item";
    public static final String WITH_MEDIA_ITEM_GRAPH = "release-with-media-item";

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
            mappedBy = Request.RELEASE_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Request request;

    @Basic(optional = false)
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Basic(optional = false)
    @Column(name = "release_type")
    @Enumerated(EnumType.STRING)
    private MediaReleaseType releaseType;

    @Basic(optional = false)
    @Column(name = "in_library")
    private boolean inLibrary;

    @Check(constraints = "season > 0")
    private Integer season;

    @Check(constraints = "episode > 0")
    private Integer episode;
}