package gazpacho.core.datasource.transmission.model.io;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class GetTorrentResponse extends Response<GetTorrentResponse.Arguments> {
    @Builder
    public record Arguments(ImmutableList<Torrent> torrents) {}
}
