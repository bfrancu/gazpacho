package gazpacho.core.datasource.transmission.model.io;

import lombok.Builder;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@SuperBuilder
@Jacksonized
public class RemoveRequest extends Request<RemoveRequest.Arguments> {
    @Builder
    public record Arguments(@Singular List<Integer> ids,
                            Boolean deleteLocalData) {}
}
