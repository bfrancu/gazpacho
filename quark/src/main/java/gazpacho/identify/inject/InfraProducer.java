package gazpacho.identify.inject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class InfraProducer {

    @Produces
    @ApplicationScoped
    ObjectMapper produceObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new GuavaModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
