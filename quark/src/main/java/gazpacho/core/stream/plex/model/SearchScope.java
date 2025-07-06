package gazpacho.core.stream.plex.model;

import lombok.NonNull;

public enum SearchScope {
    MOVIE(1),
    TV_SHOW(2),
    TV_SEASON(3),
    TV_EPISODE(4);

    private final Integer value;

    public Integer value() {
        return this.value;
    }

    SearchScope(@NonNull Integer value) {
        this.value = value;
    }
}
