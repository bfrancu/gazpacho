package gazpacho.core.datasource.transmission.model.io;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class GetRequest extends Request<GetRequest.Arguments> {
    @Builder
    public record Arguments(@Singular ImmutableList<String> fields) {}

}
