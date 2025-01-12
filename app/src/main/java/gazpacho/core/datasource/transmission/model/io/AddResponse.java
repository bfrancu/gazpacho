package gazpacho.core.datasource.transmission.model.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class AddResponse extends Response<AddResponse.Arguments> {
    @Builder
    public record Arguments(
            @JsonProperty("torrent-added")
            TorrentAdded torrentAdded,
            @JsonProperty ("torrent-duplicate")
            TorrentAdded torrentDuplicate) {}
}
