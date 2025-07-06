package gazpacho.core.persistence.service;

import gazpacho.core.model.RequestStatus;
import gazpacho.core.model.UserRequest;
import gazpacho.core.persistence.model.Request;
import gazpacho.core.persistence.model.mapper.RequestModelMapper;
import gazpacho.core.persistence.repository.RequestRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class RequestService {
    private static final Set<RequestStatus> ACTIVE_REQUEST_STATUS_SET = Set.of(
            RequestStatus.QUEUED_FOR_DOWNLOAD,
            RequestStatus.USER_CONFIRMED
    );

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private EntityFetcherService entityFetcherService;

    @Inject
    private RequestModelMapper requestModelMapper;

    @Transactional
    public UserRequest updateRequest(UserRequest userRequest) {
        if (userRequest.hasPersistenceId()) {
            Request request = entityFetcherService.fetchRequest(userRequest.persistenceId());
            request.setStatus(userRequest.requestStatus());
            return fromPersistence(requestRepository.save(request));
        }

        throw new IllegalStateException("Cannot update a request that has no persistence ID.");
    }

    @Transactional(readOnly = true)
    public boolean existsWithQuery(String query) {
        List<Request> requests = requestRepository.findAllByQuery(query);
        return requests.stream()
                .anyMatch(request -> {
                    RequestStatus status = request.getStatus();
                    return ACTIVE_REQUEST_STATUS_SET.contains(status) ||
                            RequestStatus.COMPLETED.equals(status) && request.getRelease().isInLibrary();
                        }
                );
    }

    @Transactional
    public UserRequest persist(UserRequest request) {
        return fromPersistence(requestRepository.save(toPersistence(request)));
    }

    private Request toPersistence(UserRequest userRequest) {
        return requestModelMapper.toPersistenceModel(userRequest);
    }

    private UserRequest fromPersistence(Request request) {
        return requestModelMapper.fromPersistenceModel(request);
    }
}
