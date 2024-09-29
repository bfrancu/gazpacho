package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.ResultsPage;
import info.movito.themoviedbapi.tools.TmdbException;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface ApiSearcher<T> {
    ResultsPage<T> searchResults(TmdbSearch searchApi, MediaItemQueryTokens queryTokens) throws TmdbException;

    default List<T> search(TmdbSearch searchApi, MediaItemQueryTokens queryTokens) throws TmdbException {
        ResultsPage<T> resultPage = searchResults(searchApi, queryTokens);
        List<T> results =  null != resultPage ? resultPage.getResults() : Collections.emptyList();
        return results;
    }
}

