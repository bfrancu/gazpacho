package gazpacho.core.stream.plex;

import gazpacho.core.stream.plex.model.*;
import gazpacho.core.stream.plex.model.io.*;
import lombok.NonNull;

public interface PlexClient {
    SearchResponse search(@NonNull String sectionId, @NonNull String query, @NonNull SearchScope scope);

    GetAllLibrariesResponse getLibraries();

    GetLibraryItemsResponse getLibraryItems(@NonNull String sectionId);

    GetMetadataResponse getMetadata(@NonNull String ratingKey);

    GetChildrenResponse getChildren(@NonNull String ratingKey);

    void rescan(@NonNull String sectionId);
}
