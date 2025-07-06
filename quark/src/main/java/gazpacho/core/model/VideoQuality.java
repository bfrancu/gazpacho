package gazpacho.core.model;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;

import java.util.Arrays;
import java.util.function.Function;

public enum VideoQuality {
    UHD_2160P("2160p"),
    HD_1080P("1080p"),
    HD_720P("720p"),
    UNKNOWN("");

    private static final ImmutableMap<String, VideoQuality> VALUES_MAP = Arrays.stream(VideoQuality.values())
            .filter(value -> !UNKNOWN.equals(value))
            .collect(ImmutableMap.toImmutableMap(VideoQuality::value, Function.identity()));

    private final String value;

    public static VideoQuality from(@NonNull String value) {
        if (VALUES_MAP.keySet().contains(value)) {
            return VALUES_MAP.get(value);
        }
        return VideoQuality.UNKNOWN;
    }

    public String value() {
        return value;
    }

    VideoQuality(@NonNull String value) {
        this.value = value;
    }
}
