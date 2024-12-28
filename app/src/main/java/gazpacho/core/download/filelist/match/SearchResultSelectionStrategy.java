package gazpacho.core.download.filelist.match;

import gazpacho.core.download.filelist.model.SearchResultEntry;
import gazpacho.core.model.MediaItem;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface SearchResultSelectionStrategy {
    Optional<SearchResultEntry> select(@NonNull List<SearchResultEntry> resultEntries,
                                       @NonNull MediaItem mediaItem);
}
