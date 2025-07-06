package gazpacho.core.persistence.service;

import gazpacho.core.model.UserProfile;
import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.mapper.ProfileModelMapper;
import gazpacho.core.persistence.repository.ProfileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ProfileService {
    @Inject
    ProfileRepository profileRepository;

    @Inject
    ProfileModelMapper profileModelMapper;

    @Transactional
    public UserProfile persist(@NonNull UserProfile profile) {
        if (!profileRepository.existsById(profile.id())) {
            return fromPersistence(profileRepository.save(toPersistence(profile)));
        }
        return profile;
    }

    private Profile toPersistence(UserProfile profile) {
        return profileModelMapper.toPersistenceModel(profile);
    }

    private UserProfile fromPersistence(Profile profile) {
        return profileModelMapper.fromPersistenceModel(profile);
    }
}
