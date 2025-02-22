package gazpacho.core.persistence.model;

import gazpacho.core.model.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "\"Requests\"")
public class Request extends Versioned {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number")
    private Profile originator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id")
    private MediaDataSource source;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tmdb_id")
    private MediaItem item;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private RequestStatus status;

    @Basic(optional = false)
    private LocalDateTime created;

    @Basic(optional = false)
    @Column(name = "request_query", length = 200)
    private String query;
}

