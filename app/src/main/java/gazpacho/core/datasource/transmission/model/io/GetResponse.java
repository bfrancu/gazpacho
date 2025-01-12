package gazpacho.core.datasource.transmission.model.io;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class GetResponse extends Response<GetResponse.Arguments> {
    @Builder
    public record Arguments(ImmutableList<Torrent> torrents) {}
}
