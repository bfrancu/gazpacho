package gazpacho.core.persistence.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NamedEntityGraph(
        name = MediaDataSource.WITH_REQUEST_RELEASE_PROFILE_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("request")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = MediaDataSource.WITH_REQUEST_GRAPH,
                        attributeNodes = {
                                @NamedAttributeNode("release"),
                                @NamedAttributeNode("originator")
                        }
                )
        }

)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "\"DataSources\"")
public class MediaDataSource extends Versioned {
    public static final String REQUEST_FIELD = "request";
    public static final String WITH_REQUEST_GRAPH = "request-graph";
    public static final String WITH_REQUEST_RELEASE_PROFILE_GRAPH = "datasource-with-request";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "source_id")
    private Long id;

    @Column(length=100, unique = true, name = "torrent_hash")
    private String torrentHash;

    @ToString.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "request_id")
    private Request request;

    @Basic(optional=false)
    @Column(length=100, unique = true, name = "torrent_name")
    private String torrentName;

    @Basic(optional=false)
    @Column(length=150, unique = true, name = "local_download_path")
    private String downloadLocation;

    @Basic(optional = false)
    @Column(name="download_finished")
    private Boolean finished;

    @Basic(optional = false)
    private MediaSize mediaSize;
}