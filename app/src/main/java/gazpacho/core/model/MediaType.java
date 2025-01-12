package gazpacho.core.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;

import java.util.Arrays;
import java.util.function.Function;

public enum MediaType {
    MOVIE("movie"),
    TV_SHOW("show"),
    TV_SEASON("season"),
    TV_EPISODE("episode"),
    UNKNOWN("unknown");

    private static final ImmutableMap<String, MediaType> VALUES_MAP = Arrays.stream(MediaType.values())
            .filter(value -> !UNKNOWN.equals(value))
            .collect(ImmutableMap.toImmutableMap(MediaType::value, Function.identity()));

    @JsonValue
    private final String value;

    public static MediaType from(@NonNull String value) {
        return VALUES_MAP.getOrDefault(value, MediaType.UNKNOWN);
    }

    public String value() {
        return this.value;
    }

    MediaType(@NonNull String value) {
        this.value = value;
    }
}
