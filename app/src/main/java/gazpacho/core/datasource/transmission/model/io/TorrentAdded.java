package gazpacho.core.datasource.transmission.model.io;

import lombok.Builder;

@Builder
public record TorrentAdded(String hashString,
                           Long id,
                           String name) {}