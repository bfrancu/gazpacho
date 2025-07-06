package gazpacho.core.datasource.filelist;

import gazpacho.core.datasource.MediaDataSourceRetriever;
import gazpacho.core.datasource.filelist.download.DataSourceDownloader;
import gazpacho.core.datasource.filelist.match.SearchResultSelectionStrategy;
import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.datasource.filelist.navigate.MediaSearcher;
import gazpacho.core.datasource.filelist.navigate.SessionHandler;
import gazpacho.core.exception.RetryableException;
import gazpacho.core.model.DataSource;
import gazpacho.core.model.VisualMedia;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.slf4j.Logger;

import java.io.IOException;
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
    public Optional<DataSource> retrieveDataSource(@NonNull VisualMedia visualMedia) {
        try {
            Connection connection = sessionHandler.getActiveSession();
            logger.info("Active connection established");

            List<SearchResultEntry> searchResults = mediaSearcher.searchItem(visualMedia, connection);
            logger.info("Search results {}", searchResults);
            Optional<SearchResultEntry> bestMatch = matchingStrategy.select(searchResults, visualMedia);

            if (bestMatch.isPresent()) {
                return Optional.of(downloadDataSource(bestMatch.get(), connection));
            }

        } catch (IOException | InterruptedException e) {
            throw new RetryableException("Exception encountered", e);
        }
        return Optional.empty();
    }

    private DataSource downloadDataSource(SearchResultEntry resultEntry, Connection connection) {
        return DataSource.builder()
                .name(resultEntry.title())
                .size(resultEntry.downloadSize())
                .location(dataSourceDownloader.download(resultEntry, connection).toString())
                .finished(false)
                .build();
    }
}