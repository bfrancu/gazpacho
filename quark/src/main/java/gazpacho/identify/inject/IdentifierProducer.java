package gazpacho.identify.inject;

import gazpacho.core.identify.ExternalTitleParser;
import gazpacho.core.identify.MediaIdentifier;
import gazpacho.core.identify.QueryTokensParser;
import gazpacho.core.identify.tmdb.ExternalIdMediaIdentifier;
import gazpacho.core.identify.tmdb.FirstPageSearcher;
import gazpacho.core.identify.tmdb.MostPopularMediaSelector;
import gazpacho.core.identify.tmdb.TmdbMediaIdentifier;
import gazpacho.core.identify.tmdb.TmdbMediaSelector;
import gazpacho.core.identify.tmdb.TmdbSearcher;
import gazpacho.identify.inject.qualifiers.tmdb.ExternalIdentifier;
import gazpacho.identify.inject.qualifiers.tmdb.TmdbApiReadToken;
import gazpacho.identify.inject.qualifiers.tmdb.TmdbIdentifier;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.tools.model.time.ExternalSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.slf4j.Logger;

@ApplicationScoped
public class IdentifierProducer {
    @Produces
    @ApplicationScoped
    TmdbApi produceTmdbApi(@TmdbApiReadToken String tmdbApiReadToken) {
        return new TmdbApi(tmdbApiReadToken);
    }

    @Produces
    @ApplicationScoped
    TmdbSearcher produceTmdbSearcher(TmdbApi tmdbApi, Logger logger) {
        return new FirstPageSearcher(tmdbApi, logger);
    }

    @Produces
    @ApplicationScoped
    TmdbMediaSelector produceTmdbMediaSelector(TmdbApi tmdbApi, Logger logger) {
        return new MostPopularMediaSelector(tmdbApi, logger);
    }

    @Produces
    @ApplicationScoped
    @TmdbIdentifier
    MediaIdentifier produceMediaIdentifier(TmdbSearcher tmdbSearcher,
                                           TmdbMediaSelector tmdbMediaSelector,
                                           QueryTokensParser queryTokensParser) {
        return new TmdbMediaIdentifier(tmdbSearcher, tmdbMediaSelector, queryTokensParser);
    }

    @Produces
    @ApplicationScoped
    @ExternalIdentifier
    MediaIdentifier produceExternalIdMediaIdentifier(TmdbApi tmdbApi,
                                                     ExternalTitleParser externalTitleParser,
                                                     TmdbMediaSelector tmdbMediaSelector,
                                                     ExternalSource externalSource,
                                                     Logger logger) {
        return new ExternalIdMediaIdentifier(
                tmdbApi, externalTitleParser, tmdbMediaSelector, externalSource, logger
        );
    }

}
