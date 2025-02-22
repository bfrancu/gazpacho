package gazpacho.core.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfileOverride;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = Request_.ORIGINATOR)
    @ToString.Exclude
    @FetchProfileOverride(
            profile = Profile_.PROFILE_WITH_REQUESTS_WISHLIST,
            mode = FetchMode.JOIN)
    private Set<Request> requests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = Wish_.PROFILE)
    @ToString.Exclude
    @FetchProfileOverride(
            profile = Profile_.PROFILE_WITH_REQUESTS_WISHLIST,
            mode = FetchMode.JOIN)
    private Set<Wish> wishlist;

    @Column(length = 50, name = "first_name")
    private String firstName;

    @Column(length = 50, name = "last_name")
    private String lastName;
}
