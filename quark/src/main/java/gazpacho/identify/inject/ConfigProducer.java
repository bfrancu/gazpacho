package gazpacho.identify.inject;

import gazpacho.identify.inject.qualifiers.datasource.DownloadPath;
import gazpacho.identify.inject.qualifiers.datasource.SessionPassword;
import gazpacho.identify.inject.qualifiers.datasource.SessionUser;
import gazpacho.identify.inject.qualifiers.plex.PlexHost;
import gazpacho.identify.inject.qualifiers.plex.PlexPort;
import gazpacho.identify.inject.qualifiers.plex.PlexToken;
import gazpacho.identify.inject.qualifiers.tmdb.TmdbApiReadToken;
import info.movito.themoviedbapi.tools.model.time.ExternalSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ConfigProducer {
    private static final String API_READ_ACCESS_TOKEN_KEY
            = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4OWYxOTRlYTY1NzIzZTU0ZmE4MjU5YjkzMTJkZWRkOSIsIm5iZiI6MTcyNTIxNDg2NC45ODI1MzQsInN1YiI6IjY2ZDQ4ZDMwNGZhYzE0Nzc3MzU4MmNkYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KX2rsy__4UzyZNHfLKR7-9rURil-mpk84LbBoc_qm3Q";

    private static final String LOCAL_PLEX_HOST = "localhost";
    private static final Integer PLEX_PORT = 32400;
    private static final String X_PLEX_TOKEN = "cZb_yyH6vRZ5hxSNAjKX";

    @Produces
    @ApplicationScoped
    @TmdbApiReadToken
    String produceTmdbApiReadToken() {
//        return System.getenv("TMDB_API_READ_TOKEN");
        return API_READ_ACCESS_TOKEN_KEY;
    }

    @Produces
    @ApplicationScoped
    ExternalSource produceExternalSource() {
        return ExternalSource.IMDB_ID;
    }

    @Produces
    @ApplicationScoped
    @PlexHost
    String producePlexHost() {
        return LOCAL_PLEX_HOST;
    }

    @Produces
    @ApplicationScoped
    @PlexPort
    Integer producePlexPort() {
        return PLEX_PORT;
    }

    @Produces
    @ApplicationScoped
    @PlexToken
    String producePlexToken() {
        return X_PLEX_TOKEN;
    }

    @Produces
    @ApplicationScoped
    @DownloadPath
    String produceDownloadPath() {
        return "/home/bfrancu/Downloads";
    }

    @Produces
    @ApplicationScoped
    @SessionUser
    String produceSessionUser() {
        return "flashback";
    }

    @Produces
    @ApplicationScoped
    @SessionPassword
    String produceSessionPassword() {
        return "b54nfo";
    }
}
