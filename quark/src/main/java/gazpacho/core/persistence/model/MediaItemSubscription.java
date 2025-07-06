package gazpacho.core.persistence.model;

import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@NamedEntityGraph(
        name = MediaItemSubscription.WITH_ITEM_SUBSCRIBERS_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("item"),
                @NamedAttributeNode("subscribers")
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name="\"MediaSubscriptions\"")
public class MediaItemSubscription extends Versioned {
    public static final String ITEM_FIELD = "item";
    public static final String WITH_ITEM_SUBSCRIBERS_GRAPH = "subscription-with-item-subscribers";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id")
    private Long id;

    @OneToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tmdb_id"),
            @JoinColumn(name = "media_type")})
    private MediaItem item;

    @ToString.Exclude
    @OneToMany(mappedBy = Wish.SUBSCRIPTION_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Wish> subscribers;

    @Column(name = "last_scanned")
    private LocalDate lastScanned;
}
