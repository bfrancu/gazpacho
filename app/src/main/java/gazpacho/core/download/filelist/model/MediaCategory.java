package gazpacho.core.download.filelist.model;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

public enum MediaCategory {
    ALL(0),
    MOVIES_SD(1),
    MOVIES_DVD(2),
    MOVIES_DVD_RO(3),
    MOVIES_HD(4),
    MOVIES_4K(6),
    SPORTS(13),
    MOVIES_HD_RO(19),
    MOVIES_BLURAY(20),
    TV_HD(21),
    TV_SD(23),
    MOVIES_3D(25),
    MOVIES_4K_BLURAY(26),
    TV_4K(27),
    UNKNOWN(-1);

    private static final ImmutableMap<Integer, MediaCategory> VALUES_MAP = Arrays.stream(MediaCategory.values())
            .filter(value -> !UNKNOWN.equals(value))
            .collect(ImmutableMap.toImmutableMap(MediaCategory::value, Function.identity()));

    private static final Set<MediaCategory> MOVIES = Set.of(
            MOVIES_SD,
            MOVIES_DVD,
            MOVIES_DVD_RO,
            MOVIES_HD,
            MOVIES_4K,
            MOVIES_HD_RO,
            MOVIES_BLURAY,
            MOVIES_3D,
            MOVIES_4K_BLURAY,
            SPORTS);

    private static final Set<MediaCategory> TV = Set.of(
            TV_SD,
            TV_HD,
            TV_4K
    );

    private final Integer category;

    public static MediaCategory from(Integer category) {
        if (VALUES_MAP.keySet().contains(category)) {
            return VALUES_MAP.get(category);
        }
        return MediaCategory.UNKNOWN;
    }

    public boolean isMovie() {
        return MOVIES.contains(this);
    }

    public boolean isTv() {
        return TV.contains(this);
    }

    public Integer value() {
        return category;
    }

    MediaCategory(int category) {
        this.category = category;
    }
}
