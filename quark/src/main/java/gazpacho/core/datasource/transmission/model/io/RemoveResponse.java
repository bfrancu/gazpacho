package gazpacho.core.datasource.transmission.model.io;

import gazpacho.core.model.Empty;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class RemoveResponse extends Response<Empty> {
}
