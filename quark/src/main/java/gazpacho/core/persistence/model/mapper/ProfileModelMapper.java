package gazpacho.core.persistence.model.mapper;

import gazpacho.core.model.UserProfile;
import gazpacho.core.persistence.model.Profile;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileModelMapper implements PersistenceModelMapper<UserProfile, Profile> {

    @Override
    public Profile toPersistenceModel(UserProfile domainModel) {
        return Profile.builder()
                .userId(domainModel.id())
                .firstName(domainModel.firstName())
                .lastName(domainModel.lastName())
                .build();
    }

    @Override
    public UserProfile fromPersistenceModel(Profile persistenceModel) {
        return UserProfile.builder()
                .id(persistenceModel.getUserId())
                .firstName(persistenceModel.getFirstName())
                .lastName(persistenceModel.getLastName())
                .build();
    }
}
