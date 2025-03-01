package gazpacho.core.persistence.model;

import gazpacho.core.model.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.FetchProfile;

import java.time.LocalDateTime;

@FetchProfile(name = "WithDatasourceItemProfile")
@NamedEntityGraph(
        name = "request-with-datasource-item-profile",
        attributeNodes = {
                @NamedAttributeNode("source"),
                @NamedAttributeNode("item"),
                @NamedAttributeNode("originator")
        }
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "\"Requests\"")
public class Request extends Versioned {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "request_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number")
    private Profile originator;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = MediaDataSource_.REQUEST,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private MediaDataSource source;

    @ToString.Exclude
    @ManyToOne(/*optional = false,*/ fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tmdb_id"),
            @JoinColumn(name = "media_type")})
    private MediaItem item;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private RequestStatus status;

    @Basic(optional = false)
    private LocalDateTime created;

    @Basic(optional = false)
    @Column(name = "request_query", length = 200, unique = true)
    private String query;

    @Check(constraints = "season > 0")
    private Integer season;

    @Check(constraints = "episode > 0")
    private Integer episode;
}

