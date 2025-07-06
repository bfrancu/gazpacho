package gazpacho.core.persistence.model.mapper;

import gazpacho.core.model.UserRequest;
import gazpacho.core.persistence.model.Request;
import gazpacho.core.persistence.service.EntityFetcherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RequestModelMapper implements PersistenceModelMapper<UserRequest, Request> {
    @Inject
    EntityFetcherService entityFetcherService;

    @Inject
    ProfileModelMapper profileModelMapper;

    @Inject
    ReleaseModelMapper releaseModelMapper;

    @Override
    public Request toPersistenceModel(UserRequest userRequest) {
        Request.RequestBuilder requestBuilder = Request.builder();

        if (userRequest.hasPersistenceId()) {
             requestBuilder = entityFetcherService.fetchRequest(userRequest.persistenceId())
                    .toBuilder();
        } else {
            requestBuilder = requestBuilder.originator(entityFetcherService.fetchProfile(userRequest.userProfile().id()))
                    .item(entityFetcherService.fetchMediaItem(userRequest.mediaRelease().mediaId()))
                    .release(entityFetcherService.fetchRelease(userRequest.mediaRelease()));
        }

        return requestBuilder.status(userRequest.requestStatus())
                .query(userRequest.query())
                .build();
    }

    @Override
    public UserRequest fromPersistenceModel(Request request) {
        return UserRequest.builder()
                .persistenceId(request.getId())
                .userProfile(profileModelMapper.fromPersistenceModel(request.getOriginator()))
                .mediaRelease(releaseModelMapper.fromPersistenceModel(request.getRelease()))
                .requestStatus(request.getStatus())
                .query(request.getQuery())
                .build();
    }
}
