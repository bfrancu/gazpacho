package gazpacho.core.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "\"DataSources\"")
public class MediaDataSource extends Versioned {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "data_source_id")
    private Long id;

    @ToString.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "request_id")
    private Request request;

    @Basic(optional=false)
    @Column(length=100, unique = true, name = "torrent_link")
    private String torrentLink;

    @Basic(optional=false)
    @Column(length=100, unique = true, name = "torrent_id")
    private String torrentId;

    @Basic(optional=false)
    @Column(length=150, unique = true, name = "local_download_path")
    private String downloadLocation;

    @Basic(optional = false)
    @Column(name="download_finished")
    private Boolean finished;

    @Basic(optional = false)
    private MediaSize mediaSize;
}