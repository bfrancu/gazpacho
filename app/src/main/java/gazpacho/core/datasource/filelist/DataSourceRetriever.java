package gazpacho.core.datasource.filelist;

import gazpacho.core.datasource.MediaDataSourceRetriever;
import gazpacho.core.datasource.filelist.download.DataSourceDownloader;
import gazpacho.core.datasource.filelist.match.SearchResultSelectionStrategy;
import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.datasource.filelist.navigate.MediaSearcher;
import gazpacho.core.datasource.filelist.navigate.SessionHandler;
import gazpacho.core.exception.NonRetryableException;
import gazpacho.core.exception.RetryableException;
import gazpacho.core.model.MediaItem;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

    /*
    steps:
    1. login
    2. write query url and retrieve page
    3. iterate over result pages and collect them in a set
    4. retrieve each result page and collect result items

    # Login and maintain session
    If session is already active - use that

    # Search and navigate
    Write the query url and retrieve first page
    Insert first page in a set
    Get only the first 10 available pages from the first result page and insert them in the same set

    # Collect results
    for each available search result url open it and collect all results entries in a collection

    # Choose most suitable result
    Select based on size, category and identifier matching

    # Download torrent
    Access download link, stream content to file on disk

    # Complete process
    Return path to torrent file

     */


@Slf4j
public class DataSourceRetriever implements MediaDataSourceRetriever {
    private final SessionHandler sessionHandler;
    private final MediaSearcher mediaSearcher;
    private final SearchResultSelectionStrategy matchingStrategy;
    private final DataSourceDownloader dataSourceDownloader;
    private final Logger logger;

    public DataSourceRetriever(@NonNull SessionHandler sessionHandler,
                               @NonNull MediaSearcher mediaSearcher,
                               @NonNull SearchResultSelectionStrategy matchingStrategy,
                               @NonNull DataSourceDownloader dataSourceDownloader,
                               @NonNull Logger logger) {
        this.sessionHandler = sessionHandler;
        this.mediaSearcher = mediaSearcher;
        this.matchingStrategy = matchingStrategy;
        this.dataSourceDownloader = dataSourceDownloader;
        this.logger = logger;
    }

    @Override
    public Path retrieveDataSource(@NonNull MediaItem mediaItem) {
        try {
            Connection connection = sessionHandler.getActiveSession();
            logger.info("Active connection established");
            List<SearchResultEntry> searchResults = mediaSearcher.searchItem(mediaItem, connection);
            logger.info("Search results {}", searchResults);
            Optional<SearchResultEntry> bestMatch = matchingStrategy.select(searchResults, mediaItem);
            if (bestMatch.isEmpty()) {
                throw new NonRetryableException(String.format("No file found for %s", mediaItem));
            }
            return dataSourceDownloader.download(bestMatch.get(), connection);
        } catch (IOException | InterruptedException e) {
            throw new RetryableException("Exception encountered", e);
        }
    }
}
