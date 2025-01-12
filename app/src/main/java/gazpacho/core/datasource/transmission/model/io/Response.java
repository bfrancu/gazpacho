package gazpacho.core.datasource.transmission.model.io;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
@RequiredArgsConstructor
public class Response<Arguments> {
    @NonNull
    private final String result;
    private final Arguments arguments;
    private final Integer tag;
}
