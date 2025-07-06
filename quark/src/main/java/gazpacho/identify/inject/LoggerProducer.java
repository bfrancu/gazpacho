package gazpacho.identify.inject;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LoggerProducer {
    @Produces
    @ApplicationScoped
    Logger produceLogger() {
        return LoggerFactory.getLogger(LoggerProducer.class);
    }
}
