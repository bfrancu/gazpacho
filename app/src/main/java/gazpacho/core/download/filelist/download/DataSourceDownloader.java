package gazpacho.core.download.filelist.download;

import gazpacho.core.download.filelist.model.SearchResultEntry;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;

@Slf4j
public class DataSourceDownloader {
    private static final String FILE_EXTENSION = "torrent";
    private static final String EXTENSION_SEP = ".";

    private final Path downloadPath;
    private final Logger logger;

    public DataSourceDownloader(@NonNull Path downloadPath, @NonNull Logger logger) {
        this.downloadPath = downloadPath;
        this.logger = logger;
    }

    public Path download(@NonNull SearchResultEntry resultEntry,
                         @NonNull Connection connection) {
        Path dataSourceFilePath = getDataSourceFilePath(resultEntry);

        logger.info("Downloading {} to {}", resultEntry, dataSourceFilePath);

        try (OutputStream os = new BufferedOutputStream(
                new FileOutputStream(dataSourceFilePath.toFile()))) {
           connection.url(resultEntry.downloadLink());
           Connection.Response response = connection.execute();
           try (InputStream is = response.bodyStream()) {
               is.transferTo(os);
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataSourceFilePath;
    }

    private Path getDataSourceFilePath(SearchResultEntry resultEntry) {
        String fileName = resultEntry.title() + EXTENSION_SEP + FILE_EXTENSION;
        return downloadPath.resolve(fileName);
    }

}
