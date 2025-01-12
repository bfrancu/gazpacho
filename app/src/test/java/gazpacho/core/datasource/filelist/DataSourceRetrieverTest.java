package gazpacho.core.datasource.filelist;

import gazpacho.core.datasource.filelist.download.DataSourceDownloader;
import gazpacho.core.datasource.filelist.match.SearchResultSelectionStrategy;
import gazpacho.core.datasource.filelist.model.*;
import gazpacho.core.datasource.filelist.navigate.MediaSearcher;
import gazpacho.core.datasource.filelist.navigate.SessionHandler;
import gazpacho.core.exception.NonRetryableException;
import gazpacho.core.model.MediaItem;
import gazpacho.core.model.MediaType;
import gazpacho.core.model.VideoQuality;
import org.jsoup.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSourceRetrieverTest {
    private static final SearchResultEntry SEARCH_RESULT_ENTRY = SearchResultEntry.builder()
            .title("Test.Movie")
            .detailsLink("test related")
            .downloadLink("download.link")
            .category(CategoryDetails.builder()
                    .category(MediaCategory.MOVIES_3D)
                    .label("")
                    .build())
            .downloadSize(DownloadSize.builder()
                    .size(5.0)
                    .unit(SizeUnit.GB)
                    .build())
            .videoQuality(VideoQuality.UHD_2160P)
            .seeders(256)
            .build();

    private static final MediaItem TEST_MEDIA_ITEM = MediaItem.builder()
            .title("Test movie")
            .description("")
            .firstAirDate(LocalDate.parse("1999-01-01"))
            .mediaType(MediaType.MOVIE)
            .language("EN")
            .popularity(200.0)
            .build();

    @Mock
    SessionHandler sessionHandler;

    @Mock
    MediaSearcher mediaSearcher;

    @Mock
    SearchResultSelectionStrategy matchingStrategy;

    @Mock
    DataSourceDownloader dataSourceDownloader;

    @Mock
    Logger logger;

    @Mock
    Connection connection;

    DataSourceRetriever instance;

    private DataSourceRetriever makeUnit() {
        return new DataSourceRetriever(sessionHandler,
                mediaSearcher,
                matchingStrategy,
                dataSourceDownloader,
                logger);
    }

    @BeforeEach
    public void setup() throws IOException {
        instance = makeUnit();
        when(sessionHandler.getActiveSession()).thenReturn(connection);
    }

    @Test
    public void retrieveDataSource_returnsDownloadPath() throws IOException, InterruptedException {
        when(mediaSearcher.searchItem(TEST_MEDIA_ITEM, connection))
                .thenReturn(List.of(SEARCH_RESULT_ENTRY));

        when(matchingStrategy.select(List.of(SEARCH_RESULT_ENTRY), TEST_MEDIA_ITEM))
                .thenReturn(Optional.of(SEARCH_RESULT_ENTRY));

        instance.retrieveDataSource(TEST_MEDIA_ITEM);

        verify(dataSourceDownloader, times(1)).download(SEARCH_RESULT_ENTRY, connection);
    }

    @Test
    public void retrieveDataSource_throwsNonRetryableException_whenNoMatch() throws IOException, InterruptedException {
        when(mediaSearcher.searchItem(TEST_MEDIA_ITEM, connection))
                .thenReturn(List.of());

        when(matchingStrategy.select(List.of(), TEST_MEDIA_ITEM))
                .thenReturn(Optional.empty());

        assertThrows(NonRetryableException.class, () -> instance.retrieveDataSource(TEST_MEDIA_ITEM));
    }

    @Test
    public void retrieveDataSource_throwsRetryableException_whenIOException() throws IOException, InterruptedException {
        when(mediaSearcher.searchItem(TEST_MEDIA_ITEM, connection)).thenThrow(IOException.class);
        assertThrows(RuntimeException.class, () -> instance.retrieveDataSource(TEST_MEDIA_ITEM));
    }

    @Test
    public void retrieveDataSource_throwsRetryableException_whenInterruptedException() throws IOException, InterruptedException {
        when(mediaSearcher.searchItem(TEST_MEDIA_ITEM, connection)).thenThrow(InterruptedException.class);
        assertThrows(RuntimeException.class, () -> instance.retrieveDataSource(TEST_MEDIA_ITEM));
    }
}