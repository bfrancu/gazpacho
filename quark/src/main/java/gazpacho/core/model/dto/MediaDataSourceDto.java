package gazpacho.core.model.dto;

import gazpacho.core.model.SizeUnit;

public record MediaDataSourceDto(
        String torrentHash,
        String torrentName,
        String downloadLocation,
        boolean finished,
        SizeUnit unit,
        Double size) {
}
