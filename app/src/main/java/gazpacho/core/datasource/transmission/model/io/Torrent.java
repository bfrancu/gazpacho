package gazpacho.core.datasource.transmission.model.io;

import com.google.common.collect.ImmutableList;
import lombok.Builder;

@Builder
public record Torrent(Integer id,
                      String name,
                      Status status,
                      Long addedDate,
                      ImmutableList<File> files,
                      Integer error,
                      String errorString,
                      Integer eta,
                      Boolean finished,
                      Integer leftUntilDone,
                      Boolean percentDone,
                      Long sizeWhenDone,
                      Long rateDownload,
                      Long rateUpload) {}
