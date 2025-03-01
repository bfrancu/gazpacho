package gazpacho.core.datasource.transmission.model.io;

import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class GetSessionResponse extends Response<Session> {
}
