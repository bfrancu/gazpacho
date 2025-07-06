package gazpacho.core.persistence.controller;

import gazpacho.core.model.VisualMedia;
import gazpacho.core.persistence.table.MediaItemTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaPersistenceController {
    private final MediaItemTable mediaItemTable;

    public void persist(@NonNull VisualMedia visualMedia) {

    }
}
