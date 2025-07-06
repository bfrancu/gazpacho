package gazpacho.core.datasource.transmission.model.io;

import lombok.NonNull;

public record File(@NonNull Long bytesCompleted,
                   @NonNull Long length,
                   @NonNull String name,
                   Integer beginPiece,
                   Integer endPiece) {}
