package gazpacho.core.datasource.filelist.match;

import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.model.VideoQuality;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Map;

import static java.util.Map.entry;

public class VideoQualityResultsComparator implements Comparator<SearchResultEntry> {
    private static final Map<Pair<VideoQuality, VideoQuality>, Integer> QUALITY_ORDER_MAP = Map.ofEntries(
            entry(Pair.of(VideoQuality.UNKNOWN, VideoQuality.HD_720P), 1),
            entry(Pair.of(VideoQuality.UNKNOWN, VideoQuality.HD_1080P), 1),
            entry(Pair.of(VideoQuality.UNKNOWN, VideoQuality.UHD_2160P), 1),
            entry(Pair.of(VideoQuality.UNKNOWN, VideoQuality.UNKNOWN), 0),
            entry(Pair.of(VideoQuality.HD_720P, VideoQuality.UNKNOWN), -1),
            entry(Pair.of(VideoQuality.HD_720P, VideoQuality.HD_720P), 0),
            entry(Pair.of(VideoQuality.HD_720P, VideoQuality.HD_1080P), -1),
            entry(Pair.of(VideoQuality.HD_720P, VideoQuality.UHD_2160P), -1),
            entry(Pair.of(VideoQuality.HD_1080P, VideoQuality.UNKNOWN), -1),
            entry(Pair.of(VideoQuality.HD_1080P, VideoQuality.HD_720P), 1),
            entry(Pair.of(VideoQuality.HD_1080P, VideoQuality.HD_1080P), 0),
            entry(Pair.of(VideoQuality.HD_1080P, VideoQuality.UHD_2160P), -1),
            entry(Pair.of(VideoQuality.UHD_2160P, VideoQuality.UNKNOWN), -1),
            entry(Pair.of(VideoQuality.UHD_2160P, VideoQuality.HD_720P), 1),
            entry(Pair.of(VideoQuality.UHD_2160P, VideoQuality.HD_1080P), 1),
            entry(Pair.of(VideoQuality.UHD_2160P, VideoQuality.UHD_2160P), 0)
    );


    @Override
    public int compare(SearchResultEntry left, SearchResultEntry right) {
        return QUALITY_ORDER_MAP.get(Pair.of(left.videoQuality(), right.videoQuality()));
    }

}
