package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.identify.QueryTokensParser;
import gazpacho.core.model.*;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TmdbMediaIdentifierTest {
    private static final String MOVIE_NAME = "Pirates of the Caribbean";
    private static final Integer MOVIE_YEAR = 2003;
    private static final String MOVIE_IDENTIFIER = String.format("%s|%s", MOVIE_NAME, MOVIE_YEAR);
    private static final String SHOW_NAME = "Six feet under";
    private static final String SHOW_SEASON = "S02";
    private static final String SHOW_IDENTIFIER = String.format("%s|%s", SHOW_NAME, SHOW_SEASON);
    private static final MediaItemQueryTokens MOVIE_TOKENS =
            MediaItemQueryTokens.builder()
                    .name(MOVIE_NAME)
                    .releaseYear(MOVIE_YEAR)
                    .build();

    private static final MediaItemQueryTokens SHOW_TOKENS =
            MediaItemQueryTokens.builder()
                    .name(SHOW_NAME)
                    .season(2)
                    .build();

    private static final VisualMedia MOVIE_ITEM = VisualMedia.builder()
            .metadata(MediaMetadata.builder()
                    .tmdbId(2L)
                    .title(MOVIE_NAME)
                    .mediaType(MediaType.MOVIE)
                    .description("")
                    .language("EN")
                    .firstAirDate(LocalDate.now())
                    .popularity(100.0)
                    .build())
            .release(MediaRelease.builder()
                    .mediaReleaseType(MediaReleaseType.MOVIE)
                    .build())
            .build();

    private static final VisualMedia SHOW_ITEM = VisualMedia.builder()
            .metadata(MediaMetadata.builder()
                    .tmdbId(1L)
                    .title(SHOW_NAME)
                    .mediaType(MediaType.MOVIE)
                    .description("")
                    .language("EN")
                    .firstAirDate(LocalDate.now())
                    .popularity(100.0)
                    .build())
            .release(MediaRelease.builder()
                    .mediaReleaseType(MediaReleaseType.TV_SEASON)
                    .season(2)
                    .build())
            .build();

    @Mock
    private TmdbSearcher searcher;

    @Mock
    private TmdbMediaSelector mediaSelector;

    @Mock
    private QueryTokensParser queryTokensParser;

    @Mock
    private Movie movie;

    @Mock
    private TvSeries tvSeries;

    @Test
    public void identify_returnsIdentifiedShow_whenSeasonInTokens() {
        mockShowIdentification(SHOW_IDENTIFIER, SHOW_TOKENS, SHOW_ITEM);
        var identifiedItem = makeUnit().identify(SHOW_IDENTIFIER);
        assertEquals(Optional.of(SHOW_ITEM), identifiedItem);
    }

    @Test
    public void identify_identifiesShowAndMovie_whenSeasonNotInTokens() {
        mockShowIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, SHOW_ITEM);
        mockMovieIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, MOVIE_ITEM);
        makeUnit().identify(MOVIE_IDENTIFIER);

        verify(mediaSelector).selectClosestMatch(MOVIE_ITEM, SHOW_ITEM, MOVIE_TOKENS);
    }

    @Test
    public void identify_returnsMatchedMovie_whenNullMatchedShow() {
        mockShowIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, null);
        mockMovieIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, MOVIE_ITEM);
        var identifiedItem = makeUnit().identify(MOVIE_IDENTIFIER);
        assertEquals(Optional.of(MOVIE_ITEM), identifiedItem);
    }

    @Test
    public void identify_returnsMatchedShow_whenNullMatchedMovie() {
        mockShowIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, SHOW_ITEM);
        mockMovieIdentification(MOVIE_IDENTIFIER, MOVIE_TOKENS, null);
        var identifiedItem = makeUnit().identify(MOVIE_IDENTIFIER);
        assertEquals(Optional.of(SHOW_ITEM), identifiedItem);
    }

    private void mockMovieIdentification(String identifier, MediaItemQueryTokens tokens, VisualMedia item) {
        when(queryTokensParser.parse(identifier)).thenReturn(Optional.of(tokens));
        when(searcher.searchMovies(tokens)).thenReturn(List.of(movie));
        when(mediaSelector.selectMovie(List.of(movie), tokens)).thenReturn(Optional.ofNullable(item));
    }

    private void mockShowIdentification(String identifier, MediaItemQueryTokens tokens, VisualMedia item) {
        when(queryTokensParser.parse(identifier)).thenReturn(Optional.of(tokens));
        when(searcher.searchTvSeries(tokens)).thenReturn(List.of(tvSeries));
        when(mediaSelector.selectTvSeries(List.of(tvSeries), tokens)).thenReturn(Optional.ofNullable(item));
    }

    private TmdbMediaIdentifier makeUnit() {
        return new TmdbMediaIdentifier(searcher, mediaSelector, queryTokensParser);
    }
}
