package gazpacho.core.persistence.model;

import gazpacho.core.model.RequestStatus;
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

import java.time.LocalDateTime;

@NamedEntityGraph(
        name = Request.WITH_DATASOURCE_RELEASE_PROFILE_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("source"),
                @NamedAttributeNode("release"),
                @NamedAttributeNode("originator")
        }
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "\"Requests\"")
public class Request extends Versioned {
    public static final String RELEASE_FIELD = "release";
    public static final String ITEM_FIELD = "item";
    public static final String ORIGINATOR_FIELD = "originator";
    public static final String WITH_DATASOURCE_RELEASE_PROFILE_GRAPH = "request-with-datasource-item-profile";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "request_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Profile originator;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = MediaDataSource.REQUEST_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private MediaDataSource source;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tmdb_id"),
            @JoinColumn(name = "media_type")})
    private MediaItem item;

    @ToString.Exclude
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id")
    private Release release;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private RequestStatus status;

    @Basic(optional = false)
    private LocalDateTime created;

    @Basic(optional = false)
    @Column(name = "request_query", length = 200, unique = true)
    private String query;
}
