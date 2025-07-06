package gazpacho.core.persistence.service;

import gazpacho.core.model.UserProfile;
import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.mapper.ProfileModelMapper;
import gazpacho.core.persistence.repository.ProfileRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProfileServiceTest {

    ProfileRepository profileRepository = mock(ProfileRepository.class);
    ProfileModelMapper profileModelMapper = mock(ProfileModelMapper.class);
    ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(profileRepository, profileModelMapper);
    }

    @Test
    void persist_createsNewUserProfileWhenIdDoesNotExist() {
        UserProfile userProfile = new UserProfile("newId", "John", "Doe");
        Profile profileEntity = new Profile("newId");
        profileEntity.setFirstName("John");
        profileEntity.setLastName("Doe");

        when(profileRepository.existsById("newId")).thenReturn(false);
        when(profileModelMapper.toPersistenceModel(userProfile)).thenReturn(profileEntity);
        when(profileRepository.save(profileEntity)).thenReturn(profileEntity);
        when(profileModelMapper.fromPersistenceModel(profileEntity)).thenReturn(userProfile);

        UserProfile result = profileService.persist(userProfile);

        assertEquals("newId", result.id());
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
    }

    @Test
    void persist_returnsExistingUserProfileWhenIdExists() {
        UserProfile userProfile = new UserProfile("existingId", "Jane", "Doe");
        when(profileRepository.existsById("existingId")).thenReturn(true);

        UserProfile result = profileService.persist(userProfile);

        assertEquals("existingId", result.id());
        assertEquals("Jane", result.firstName());
        assertEquals("Doe", result.lastName());
    }

    @Test
    void persist_throwsExceptionForNullUserProfile() {
        assertThrows(NullPointerException.class, () -> profileService.persist(null));
    }

}