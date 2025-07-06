package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.model.Profile;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ProfileRepositoryTest {

    @Inject
    private ProfileRepository profileRepository;

    @Test
    public void getProfileByUserId_returnsProfileWithRequestsAndWishlist() {
        // Arrange
        Profile profile = Profile.builder()
                .userId("user123")
                .firstName("John")
                .lastName("Doe")
                .build();
        profileRepository.save(profile);

        // Act
        Optional<Profile> optProfile = profileRepository.getProfileByUserId("user123");

        // Assert
        assertTrue(optProfile.isPresent());
        Profile found = optProfile.get();
        assertEquals("user123", found.getUserId());
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
        assertNotNull(found.getRequests());
        assertNotNull(found.getWishlist());
    }

    @Test
    public void getProfileByUserId_returnsEmptyForUnknownUser() {
        Optional<Profile> optProfile = profileRepository.getProfileByUserId("unknown");
        assertTrue(optProfile.isEmpty());
    }
}