package gazpacho.core.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfileOverride;

import java.time.LocalDateTime;
import java.util.Set;

@FetchProfile(name = "WithItemSubscribers")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name="\"MediaSubscriptions\"")
public class MediaItemSubscription extends Versioned {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id")
    private Long id;

    @OneToOne(optional = false, fetch=FetchType.LAZY)
    @FetchProfileOverride(
            profile = MediaItemSubscription_.PROFILE_WITH_ITEM_SUBSCRIBERS,
            mode = FetchMode.JOIN)
    @JoinColumn(name = "tmdb_id")
    private MediaItem item;

    @ToString.Exclude
    @OneToMany(mappedBy = Wish_.SUBSCRIPTION,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @FetchProfileOverride(
            profile = MediaItemSubscription_.PROFILE_WITH_ITEM_SUBSCRIBERS,
            mode = FetchMode.SUBSELECT)
    private Set<Wish> subscribers;

    @Column(name = "last_scanned")
    private LocalDateTime lastScanned;
}

