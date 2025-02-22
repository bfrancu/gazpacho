package gazpacho.core.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;

import java.util.Arrays;
import java.util.function.Function;

public enum MediaReleaseType {
    MOVIE("movie"),
    TV_SHOW("show"),
    TV_SEASON("season"),
    TV_EPISODE("episode"),
    UNKNOWN("unknown");

    private static final ImmutableMap<String, MediaReleaseType> VALUES_MAP = Arrays.stream(MediaReleaseType.values())
            .filter(value -> !UNKNOWN.equals(value))
            .collect(ImmutableMap.toImmutableMap(MediaReleaseType::value, Function.identity()));

    @JsonValue
    private final String value;

    public static MediaReleaseType from(@NonNull String value) {
        return VALUES_MAP.getOrDefault(value, MediaReleaseType.UNKNOWN);
    }

    public String value() {
        return this.value;
    }

    MediaReleaseType(@NonNull String value) {
        this.value = value;
    }
}
