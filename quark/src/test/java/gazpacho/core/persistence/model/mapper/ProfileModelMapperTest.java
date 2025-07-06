package gazpacho.core.persistence.model.mapper;


import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.repository.ProfileRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ProfileModelMapperTest {
    @Inject
    private ProfileRepository profileRepository;

    @Test
    public void getProfileByUserId_returnsProfileWithEmptyFields() {
        // Arrange
        Profile profile = Profile.builder()
                .userId("user456")
                .firstName("")
                .lastName("")
                .build();
        profileRepository.save(profile);

        // Act
        Optional<Profile> optProfile = profileRepository.getProfileByUserId("user456");

        // Assert
        assertTrue(optProfile.isPresent());
        Profile found = optProfile.get();
        assertEquals("user456", found.getUserId());
        assertEquals("", found.getFirstName());
        assertEquals("", found.getLastName());
        assertNotNull(found.getRequests());
        assertNotNull(found.getWishlist());
    }

    @Test
    public void getProfileByUserId_handlesNullUserId() {
        // Act
        Optional<Profile> optProfile = profileRepository.getProfileByUserId(null);

        // Assert
        assertTrue(optProfile.isEmpty());
    }

    @Test
    public void saveProfile_throwsExceptionForNullProfile() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> profileRepository.save(null));
    }

    @Test
    public void getProfileByUserId_handlesSpecialCharactersInUserId() {
        // Arrange
        Profile profile = Profile.builder()
                .userId("user!@#")
                .firstName("Special")
                .lastName("Chars")
                .build();
        profileRepository.save(profile);

        // Act
        Optional<Profile> optProfile = profileRepository.getProfileByUserId("user!@#");

        // Assert
        assertTrue(optProfile.isPresent());
        Profile found = optProfile.get();
        assertEquals("user!@#", found.getUserId());
        assertEquals("Special", found.getFirstName());
        assertEquals("Chars", found.getLastName());
        assertNotNull(found.getRequests());
        assertNotNull(found.getWishlist());
    }
}
