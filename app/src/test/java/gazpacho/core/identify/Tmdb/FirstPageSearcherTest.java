package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.TvSeriesResultsPage;
import info.movito.themoviedbapi.tools.TmdbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirstPageSearcherTest {
    private static final String QUERIED_TITLE = "Some media name";
    private static final Integer RELEASE_YEAR = 1997;

    @Mock
    private Logger logger;

    @Mock
    private TmdbApi tmdbApi;

    @Mock
    private TmdbSearch tmdbSearchApi;

    @BeforeEach
    public void setUp() {
        when(tmdbApi.getSearch()).thenReturn(tmdbSearchApi);
    }

    @Test
    public void searchMovies_makesApiCallOnce() throws TmdbException {
        MovieResultsPage resultsPage = Mockito.mock(MovieResultsPage.class);
        when(resultsPage.getResults()).thenReturn(Collections.emptyList());

        when(tmdbSearchApi.searchMovie(anyString(), anyBoolean(), any(), any(), any(), any(), any()))
                .thenReturn(resultsPage);

        MediaItemQueryTokens queryTokens = MediaItemQueryTokens.builder()
                .name(QUERIED_TITLE)
                .releaseYear(RELEASE_YEAR)
                .build();

        makeUnit().searchMovies(queryTokens);

        verify(tmdbSearchApi, times(1)).
                searchMovie(QUERIED_TITLE, false, null, String.valueOf(RELEASE_YEAR), null, null, null);
    }

    @Test
    public void searchTvSeries_makesApiCallOnce() throws TmdbException {
        TvSeriesResultsPage resultsPage = Mockito.mock(TvSeriesResultsPage.class);
        when(resultsPage.getResults()).thenReturn(Collections.emptyList());

        when(tmdbSearchApi.searchTv(anyString(), anyInt(), any(), any(), any(), any()))
                .thenReturn(resultsPage);

        MediaItemQueryTokens queryTokens = MediaItemQueryTokens.builder()
                .name(QUERIED_TITLE)
                .releaseYear(RELEASE_YEAR)
                .build();

        makeUnit().searchTvSeries(queryTokens);

        verify(tmdbSearchApi, times(1)).
                searchTv(QUERIED_TITLE, RELEASE_YEAR, false, null, null, null);

    }

    private FirstPageSearcher makeUnit() {
        return new FirstPageSearcher(tmdbApi, logger);
    }
}
