package gazpacho.core.persistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import java.util.Set;


@NamedEntityGraph(
        name = Profile.WITH_REQUESTS_WISHLIST_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("requests"),
                @NamedAttributeNode("wishlist")
        }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "\"UserProfiles\"")
public class Profile extends Versioned {
    public static final String WITH_REQUESTS_WISHLIST_GRAPH = "profile-with-requests-wishlist";

    @Id
    @NonNull
    @EqualsAndHashCode.Include
    @Column(unique = true, name = "user_id")
    private String userId;

    public Profile(@NonNull String userId) {
        this.userId = userId;
    }

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = Request.ORIGINATOR_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Request> requests;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = Wish.PROFILE_FIELD,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Wish> wishlist;

    @Column(length = 50, name = "first_name")
    private String firstName;

    @Column(length = 50, name = "last_name")
    private String lastName;
}
