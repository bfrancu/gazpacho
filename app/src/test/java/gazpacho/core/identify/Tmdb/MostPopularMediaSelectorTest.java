package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.model.MediaItem;
import gazpacho.core.model.MediaReleaseType;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.TmdbTvSeries;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.ProductionCountry;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import info.movito.themoviedbapi.tools.TmdbException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MostPopularMediaSelectorTest {
    @Mock
    private TmdbMovies tmdbMoviesApi;

    @Mock
    private TmdbTvSeries tmdbTvSeriesApi;

    @Mock
    private TmdbTvSeasons tmdbTvSeasonsApi;

    @Mock
    private TmdbApi tmdbApi;

    @Mock
    private Logger logger;

    private static final String ORIGINAL_LANG = "EN";

    @Test
    public void selectMovie_selectsMostPopularMovie() throws TmdbException {
        when(tmdbApi.getMovies()).thenReturn(tmdbMoviesApi);
        mockMoviesApi(15, "15", (double) 15);
        var optionalMediaItem = makeUnit().selectMovie(buildMovieList(15),
                buildQueryTokens(null, null));
        assertTrue(optionalMediaItem.isPresent());
        assertEquals(15, optionalMediaItem.get().popularity());
    }

    @Test
    public void selectTvSeries_selectsMostPopularTvSeries() throws TmdbException {
        when(tmdbApi.getTvSeries()).thenReturn(tmdbTvSeriesApi);
        mockTvSeriesApi(10, "10", (double) 10, 5, 12);
        var optionalMediaItem = makeUnit().selectTvSeries(buildTvSeries(10),
                buildQueryTokens(5, 12));
        assertTrue(optionalMediaItem.isPresent());
        assertEquals(10, optionalMediaItem.get().popularity());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void selectTvSeries_doesNotSelectSeries_whenSeasonsDontMatch() throws TmdbException {
        when(tmdbApi.getTvSeries()).thenReturn(tmdbTvSeriesApi);
        mockTvSeriesApi(10, "10", (double) 10, 2, 12);
        var optionalMediaItem = makeUnit().selectTvSeries(buildTvSeries(10),
                buildQueryTokens(7, 12));
        assertFalse(optionalMediaItem.isPresent());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void selectTvSeries_doesNotSelectSeries_whenEpisodesDontMatch() throws TmdbException {
        when(tmdbApi.getTvSeries()).thenReturn(tmdbTvSeriesApi);
        mockTvSeriesApi(10, "10", (double) 10, 2, 12);
        var optionalMediaItem = makeUnit().selectTvSeries(buildTvSeries(10),
                buildQueryTokens(2, 13));
        assertFalse(optionalMediaItem.isPresent());
    }

    @Test
    public void selectClosestMatch_selectsMovie_whenFullMatch() {
        MediaItem matchedMovie = MediaItem.builder()
                .title("No country for old men")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .popularity(10.0)
                .build();

        MediaItem matchedShow = MediaItem.builder()
                .title("No country for young men")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.TV_SEASON)
                .popularity(11.0)
                .season(5)
                .build();

        assertEquals(matchedMovie, makeUnit().selectClosestMatch(matchedMovie, matchedShow,
                MediaItemQueryTokens.builder()
                        .name("no country for old men")
                        .build()));
    }

    @Test
    public void selectClosestMatch_selectsShow_whenFullMatch() {
        MediaItem matchedShow = MediaItem.builder()
                .title("Breaking bad")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.TV_SEASON)
                .popularity(10.0)
                .season(5)
                .build();

        MediaItem matchedMovie = MediaItem.builder()
                .title("Breaking bread")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .popularity(100.0)
                .build();

        assertEquals(matchedShow, makeUnit().selectClosestMatch(matchedMovie, matchedShow,
                MediaItemQueryTokens.builder()
                        .name("breaking bad")
                        .build()));
    }

    @Test
    public void selectClosestMatch_selectsItemWithThresholdTitleSimilarity() {
        String queryTitle = "Lord of the rings";

        MediaItem matchedShow = MediaItem.builder()
                .title("Sopranos")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.parse("1999-05-01"))
                .mediaReleaseType(MediaReleaseType.TV_SEASON)
                .popularity(10.0)
                .season(5)
                .build();

        MediaItem matchedMovie = MediaItem.builder()
                .title("Lord of the rings, The two towers")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .popularity(100.0)
                .build();

        assertEquals(matchedMovie, makeUnit().selectClosestMatch(matchedMovie, matchedShow,
                MediaItemQueryTokens.builder()
                        .name(queryTitle)
                        .build()));
    }

    @Test
    public void selectClosestMatch_selectsMostPopularItem_whenBothHaveThresholdTitleSimilarity() {
        String title = "Grinch stole something";

        MediaItem firstMatch = MediaItem.builder()
                .title("Grinch stole Christmas")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.parse("1999-05-01"))
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .popularity(100.0)
                .build();

        MediaItem popularMatch = MediaItem.builder()
                .title("Grinch stole my heart")
                .description("")
                .language(ORIGINAL_LANG)
                .firstAirDate(LocalDate.now())
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .popularity(500.0)
                .build();

        assertEquals(popularMatch, makeUnit().selectClosestMatch(firstMatch, popularMatch,
                MediaItemQueryTokens.builder()
                        .name(title)
                        .build()));
    }

    private MostPopularMediaSelector makeUnit() {
        return new MostPopularMediaSelector(tmdbApi, logger);
    }

    private List<Movie> buildMovieList(int size) {
        List<Movie> movies = new ArrayList<>();
        for (int i = size; i > 0; --i) {
            movies.add(buildMovie(i,1.0 * i));
        }
        Collections.shuffle(movies);
        return movies;
    }

    private List<TvSeries> buildTvSeries(int size) {
        List<TvSeries> series = new ArrayList<>();
        for (int i = 1; i <= size; ++i) {
            series.add(buildSeries(i, 1.0 * i));
        }
        Collections.shuffle(series);
        return series;
    }

    private Movie buildMovie(int id, Double popularity) {
        var movie = new Movie();
        movie.setOriginalLanguage(ORIGINAL_LANG);
        movie.setId(id);
        movie.setPopularity(popularity);
        return movie;
    }

    private TvSeries buildSeries(int id, Double popularity) {
        var show = new TvSeries();
        show.setOriginalLanguage(ORIGINAL_LANG);
        show.setId(id);
        show.setPopularity(popularity);
        return show;
    }

    private void mockMoviesApi(int id, String title, Double popularity) throws TmdbException {
        var movieDetails = new MovieDb();
        movieDetails.setTitle(title);
        movieDetails.setReleaseDate("2005-03-11");
        movieDetails.setPopularity(popularity);
        movieDetails.setOverview("test");
        movieDetails.setOriginalLanguage(ORIGINAL_LANG);

        var productionCountry = new ProductionCountry();
        productionCountry.setIsoCode("581249");
        productionCountry.setName("US");
        movieDetails.setProductionCountries(List.of(productionCountry));

        when(tmdbMoviesApi.getDetails(eq(id), eq(ORIGINAL_LANG))).thenReturn(movieDetails);
    }

    private void mockTvSeriesApi(int id, String title, Double popularity, int season, int episode) throws TmdbException {
        var showDetails = new TvSeriesDb();
        showDetails.setName(title);
        showDetails.setOverview("test");
        showDetails.setOriginalLanguage(ORIGINAL_LANG);
        showDetails.setFirstAirDate("2020-02-20");
        showDetails.setPopularity(popularity);
        showDetails.setNumberOfSeasons(season);

        var seasonDetailsMock = Mockito.mock(TvSeasonDb.class);
        var episodeList = Mockito.mock(List.class);
        when(episodeList.size()).thenReturn(episode);
        when(seasonDetailsMock.getEpisodes()).thenReturn(episodeList);

        when(tmdbTvSeriesApi.getDetails(eq(id), eq(ORIGINAL_LANG))).thenReturn(showDetails);
        when(tmdbApi.getTvSeasons()).thenReturn(tmdbTvSeasonsApi);
        when(tmdbTvSeasonsApi.getDetails(eq(id), eq(season), eq(showDetails.getOriginalLanguage())))
                .thenReturn(seasonDetailsMock);
    }

    private MediaItemQueryTokens buildQueryTokens(Integer season, Integer episode) {
        return MediaItemQueryTokens.builder()
                .name("test")
                .releaseYear(2001)
                .season(season)
                .episode(episode)
                .build();
    }

}
