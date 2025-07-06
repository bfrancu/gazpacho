package gazpacho.identify.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import gazpacho.core.match.MediaMatcher;
import gazpacho.core.stream.plex.HttpPlexClient;
import gazpacho.core.stream.plex.PlexClient;
import gazpacho.core.stream.plex.match.PlexMediaMatcher;
import gazpacho.identify.inject.qualifiers.plex.PlexHost;
import gazpacho.identify.inject.qualifiers.plex.PlexPort;
import gazpacho.identify.inject.qualifiers.plex.PlexToken;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.slf4j.Logger;

@ApplicationScoped
public class PlexProducer {
    @Produces
    @ApplicationScoped
    PlexClient producePlexClient(Logger logger, ObjectMapper objectMapper,
                                  @PlexHost String host, @PlexPort Integer port,
                                  @PlexToken String plexToken) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        return new HttpPlexClient(
                httpClient,
                objectMapper,
                logger,
                port,
                host,
                plexToken
        );
    }

    @Produces
    @ApplicationScoped
    MediaMatcher producePlexMediaMatcher(PlexClient plexClient) {
        return new PlexMediaMatcher(plexClient);
    }
}
