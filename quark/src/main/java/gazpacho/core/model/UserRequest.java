package gazpacho.core.model;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record UserRequest(@NonNull MediaRelease mediaRelease,
                          @NonNull UserProfile userProfile,
                          @NonNull RequestStatus requestStatus,
                          @NonNull String query,
                          String sourceId,
                          Long persistenceId) {

    public static class UserRequestBuilder {
        Long persistenceId = 0L;
    }

    public boolean hasPersistenceId() {
        return persistenceId != null && persistenceId > 0;
    }
}
