package gazpacho.core.datasource.transmission.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Arrays;
import java.util.List;

@Builder
public record Session(
                      @JsonProperty("dht-enabled")
                      Boolean dhtEnabled,
                      @JsonProperty("pex-enabled")
                      Boolean pexEnabled) {

    public static final List<String> FIELD_NAMES = Arrays.asList(
            "dht-enabled",
            "pex-enabled"
    );
}
