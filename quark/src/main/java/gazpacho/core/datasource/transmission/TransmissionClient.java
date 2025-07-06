package gazpacho.core.datasource.transmission;

import com.google.common.collect.ImmutableList;
import gazpacho.core.datasource.transmission.model.io.Session;
import gazpacho.core.datasource.transmission.model.io.Torrent;
import gazpacho.core.datasource.transmission.model.io.TorrentAdded;
import lombok.NonNull;

import java.nio.file.Path;
import java.util.List;

public interface TransmissionClient {
    Session getSessionMetadata();

    void setSessionMetadata(@NonNull Session sessionMetadata);

    TorrentAdded download(@NonNull Path dataSourcePath, @NonNull Path downloadPath);

    void remove(@NonNull List<Integer> ids, boolean deleteLocalData);

    default void remove(@NonNull List<Integer> ids) {
        remove(ids, false);
    }

    ImmutableList<Torrent> listActive();
}
