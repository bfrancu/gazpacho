package gazpacho.core.persistence.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import java.util.Set;


@FetchProfile(name = "WithRequestsWishlist")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "\"UserProfiles\"")
public class Profile extends Versioned {
    @Id
    @NonNull
    @Column(unique = true, name = "phone_number")
    private String phoneNumber;

    public Profile(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = Request_.ORIGINATOR,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @FetchProfileOverride(
            profile = Profile_.PROFILE_WITH_REQUESTS_WISHLIST,
            mode = FetchMode.JOIN)
    private Set<Request> requests;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = Wish_.PROFILE,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @FetchProfileOverride(
            profile = Profile_.PROFILE_WITH_REQUESTS_WISHLIST,
            mode = FetchMode.JOIN)
    private Set<Wish> wishlist;

    @Column(length = 50, name = "first_name")
    private String firstName;

    @Column(length = 50, name = "last_name")
    private String lastName;
}
