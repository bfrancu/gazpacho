package gazpacho.core.datasource.transmission.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class AddRequest extends Request<AddRequest.Arguments> {
    @Builder
    public record Arguments(String filename,
                            @JsonProperty("download-dir")
                            String downloadDir) {
    }
}
