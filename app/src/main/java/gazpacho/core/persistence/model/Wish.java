package gazpacho.core.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NamedEntityGraph(
        name = "wish-with-subscription-profile",
        attributeNodes = {
                @NamedAttributeNode("subscription"),
                @NamedAttributeNode("profile")
        }
)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "\"Wishes\"")
public class Wish extends Versioned {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private MediaItemSubscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number")
    private Profile profile;
}
