package gazpacho.identify.inject;

import gazpacho.core.datasource.MediaDataSourceRetriever;
import gazpacho.core.datasource.filelist.DataSourceRetriever;
import gazpacho.core.datasource.filelist.download.DataSourceDownloader;
import gazpacho.core.datasource.filelist.match.DownloadSizeResultsComparator;
import gazpacho.core.datasource.filelist.match.ItemQueryConverter;
import gazpacho.core.datasource.filelist.match.MatchingIdentifierSelectionStrategy;
import gazpacho.core.datasource.filelist.match.SearchResultSelectionStrategy;
import gazpacho.core.datasource.filelist.match.SeedersCountResultsComparator;
import gazpacho.core.datasource.filelist.match.VideoQualityResultsComparator;
import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.datasource.filelist.navigate.MediaSearcher;
import gazpacho.core.datasource.filelist.navigate.QueryUrlResolver;
import gazpacho.core.datasource.filelist.navigate.SessionHandler;
import gazpacho.core.util.OrderedHierarchyComparator;
import gazpacho.identify.inject.qualifiers.datasource.DownloadPath;
import gazpacho.identify.inject.qualifiers.datasource.SessionPassword;
import gazpacho.identify.inject.qualifiers.datasource.SessionUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class DataSourceRetrieverProducer {
    private static final Integer SESSION_TIMEOUT_S = 300;
    private static final Integer MAX_BODY_SIZE_MB = 40;

    @Produces
    @ApplicationScoped
    DataSourceDownloader produceDataSourceDownloader(@DownloadPath String downloadPath, Logger logger) {
        return new DataSourceDownloader(Path.of(downloadPath), logger);
    }

    @Produces
    @ApplicationScoped
    SessionHandler produceSessionHandler(@SessionUser String user, @SessionPassword String password) {
        return new SessionHandler(user, password, SESSION_TIMEOUT_S, MAX_BODY_SIZE_MB);
    }

    @Produces
    @ApplicationScoped
    Comparator<SearchResultEntry> produceSearchResultEntryComparator() {
        return new OrderedHierarchyComparator<>(
                List.of(new VideoQualityResultsComparator(),
                        new DownloadSizeResultsComparator(),
                        new SeedersCountResultsComparator())
        );
    }

    @Produces
    @ApplicationScoped
    ItemQueryConverter produceItemQueryConverter() {
        return new ItemQueryConverter();
    }

    @Produces
    @ApplicationScoped
    SearchResultSelectionStrategy produceSearchResultSelectionStrategy(
            Logger logger,
            ItemQueryConverter itemQueryConverter,
            Comparator<SearchResultEntry> searchResultEntryComparator) {

        return new MatchingIdentifierSelectionStrategy(
          itemQueryConverter,
          searchResultEntryComparator,
          logger);
    }

    @Produces
    @ApplicationScoped
    MediaDataSourceRetriever produceDataSourceRetriever(
            Logger logger,
            ItemQueryConverter itemQueryConverter,
            SearchResultSelectionStrategy searchResultSelectionStrategy,
            SessionHandler sessionHandler,
            DataSourceDownloader dataSourceDownloader) {

        QueryUrlResolver queryUrlResolver = new QueryUrlResolver(itemQueryConverter);
        MediaSearcher mediaSearcher = new MediaSearcher(queryUrlResolver, logger);

        return new DataSourceRetriever(sessionHandler,
                mediaSearcher,
                searchResultSelectionStrategy,
                dataSourceDownloader,
                logger);
    }
}
