package gazpacho.core.identify.tmdb;

import gazpacho.core.identify.ExternalTitleParser;
import gazpacho.core.identify.ExternalTitleTokens;
import gazpacho.core.model.MediaId;
import gazpacho.core.model.MediaMetadata;
import gazpacho.core.model.MediaRelease;
import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.MediaType;
import gazpacho.core.model.VisualMedia;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.model.find.FindMovie;
import info.movito.themoviedbapi.model.find.FindResults;
import info.movito.themoviedbapi.tools.TmdbException;
import info.movito.themoviedbapi.tools.model.time.ExternalSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class ExternalIdMediaIdentifierTest {
    private static final ExternalTitleTokens TITLE_TOKENS = ExternalTitleTokens.builder()
            .titleId("external")
            .season(0)
            .build();

    private final TmdbApi tmdbApi = mock(TmdbApi.class);
    private final TmdbFind tmdbFind = mock(TmdbFind.class);
    private final ExternalTitleParser externalTitleParser = mock(ExternalTitleParser.class);
    private final TmdbMediaSelector mediaSelector = mock(TmdbMediaSelector.class);
    private final Logger logger = mock(Logger.class);
    private final ExternalSource externalSource = ExternalSource.IMDB_ID;

    private final ExternalIdMediaIdentifier identifier = new ExternalIdMediaIdentifier(
            tmdbApi, externalTitleParser, mediaSelector, externalSource, logger
    );

    @Test
    public void identify_returnsEmpty_whenTitleTokensNotPresent() {
        String identifierString = "invalid_identifier";
        when(externalTitleParser.parse(identifierString)).thenReturn(Optional.empty());

        Optional<VisualMedia> result = identifier.identify(identifierString);

        assertEquals(Optional.empty(), result);
        verifyNoInteractions(tmdbFind, mediaSelector);
    }

    @Test
    public void identify_returnsEmpty_whenFindResultsAreEmpty() throws TmdbException {
        String identifierString = "valid_identifier";
        FindResults findResults = mock(FindResults.class);

        when(externalTitleParser.parse(identifierString)).thenReturn(Optional.of(TITLE_TOKENS));
        when(tmdbApi.getFind()).thenReturn(tmdbFind);
        when(tmdbFind.findById(anyString(), eq(externalSource), isNull())).thenReturn(findResults);
        when(findResults.getMovieResults()).thenReturn(List.of());
        when(findResults.getTvSeriesResults()).thenReturn(List.of());
        when(findResults.getTvSeasonResults()).thenReturn(List.of());
        when(findResults.getTvEpisodeResults()).thenReturn(List.of());

        Optional<VisualMedia> result = identifier.identify(identifierString);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void identify_returnsVisualMedia_whenMovieResultsArePresent() throws TmdbException {
        String identifierString = "movie_identifier";
        FindResults findResults = mock(FindResults.class);
        VisualMedia expectedMedia = VisualMedia.builder()
                .mediaId(MediaId.builder()
                        .tmdbId(1L)
                        .mediaType(MediaType.TV)
                        .build())
                .metadata(MediaMetadata.builder()
                        .title("")
                        .description("")
                        .language("EN")
                        .firstAirDate(LocalDate.now())
                        .popularity(100.0)
                        .build())
                .release(MediaRelease.builder()
                        .mediaReleaseType(MediaReleaseType.TV_SHOW)
                        .build())
                .build();

        when(externalTitleParser.parse(identifierString)).thenReturn(Optional.of(TITLE_TOKENS));
        when(tmdbApi.getFind()).thenReturn(tmdbFind);
        when(tmdbFind.findById(anyString(), eq(externalSource), isNull())).thenReturn(findResults);
        when(findResults.getMovieResults()).thenReturn(List.of(mock(FindMovie.class)));
        when(mediaSelector.selectMovie(anyList(), any())).thenReturn(Optional.of(expectedMedia));

        Optional<VisualMedia> result = identifier.identify(identifierString);

        assertEquals(Optional.of(expectedMedia), result);
    }

    @Test
    public void identify_logsError_whenTmdbExceptionOccurs() throws TmdbException {
        String identifierString = "error_identifier";

        when(externalTitleParser.parse(identifierString)).thenReturn(Optional.of(TITLE_TOKENS));
        when(tmdbApi.getFind()).thenReturn(tmdbFind);
        when(tmdbFind.findById(anyString(), eq(externalSource), isNull())).thenThrow(new TmdbException("Test exception"));

        Optional<VisualMedia> result = identifier.identify(identifierString);

        assertEquals(Optional.empty(), result);
        verify(logger).error(eq("Exception encountered"), any(TmdbException.class));
    }
}