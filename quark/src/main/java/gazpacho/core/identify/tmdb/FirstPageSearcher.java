package gazpacho.core.identify.tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.tools.TmdbException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FirstPageSearcher implements TmdbSearcher {
    @NonNull private final TmdbApi tmdbApi;
    @NonNull private final Logger logger;

    @Override
    public List<Movie> searchMovies(@NonNull MediaItemQueryTokens queryTokens) {
        logger.info("Searching movies {}", queryTokens);
        return search(getSearchApi(), queryTokens, (searchApi, tokens) ->
                searchApi.searchMovie(tokens.name(),
                        false,
                        null,
                        (null != tokens.releaseYear()) ? String.valueOf(tokens.releaseYear()) : null,
                        null,
                        null,
                        null)
        );
    }

    @Override
    public List<TvSeries> searchTvSeries(@NonNull MediaItemQueryTokens queryTokens) {
        logger.info("Searching shows {}", queryTokens);
        return search(getSearchApi(), queryTokens, (searchApi, tokens) ->
                searchApi.searchTv(tokens.name(),
                        tokens.releaseYear(),
                        false,
                        null,
                        null,
                        null)
        );
    }

    private <T> List<T> search(TmdbSearch searchApi, MediaItemQueryTokens queryTokens, ApiSearcher<T> searcher) {
        List<T> results = new ArrayList<>();
        try {
            results = searcher.search(searchApi, queryTokens);
        } catch (TmdbException e) {
            logger.error("TmdbException caught", e);
        }
        return results;
    }

    private TmdbSearch getSearchApi() {
        return tmdbApi.getSearch();
    }
}
