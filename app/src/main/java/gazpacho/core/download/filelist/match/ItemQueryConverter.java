package gazpacho.core.download.filelist.match;

import gazpacho.core.model.MediaItem;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class ItemQueryConverter {
    private static final String QUERY_SEARCH_VALUE_DELIM = "+";
    private static final String SEARCH_RESULT_TITLE_DELIM = ".";
    private static final String EPISODE_PREFIX = "E";
    private static final String SEASON_PREFIX = "S";

    public String getQuerySearchValue(@NonNull MediaItem mediaItem) {
        return getTextRepresentation(mediaItem, QUERY_SEARCH_VALUE_DELIM);
    }

    public String getSearchResultTitle(@NonNull MediaItem mediaItem) {
        return getTextRepresentation(mediaItem, SEARCH_RESULT_TITLE_DELIM);
    }

    private String getTextRepresentation(MediaItem mediaItem, String delim) {
        return (switch(mediaItem.mediaType()) {
            case MOVIE -> getMovieQuery(mediaItem, delim);
            case TV_SEASON -> getTvSeasonQuery(mediaItem, delim);
            case TV_EPISODE -> getTvEpisodeQuery(mediaItem, delim);
        });
    }


    private String getMovieQuery(MediaItem mediaItem, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(mediaItem.title(), delim));
        builder.append(delim)
                .append(mediaItem.firstAirDate());
        return builder.toString();
    }

    private String getTvSeasonQuery(MediaItem mediaItem, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(mediaItem.title(), delim));
        builder.append(getSeasonSearchComponent(mediaItem));
        return builder.toString();
    }

    private String getTvEpisodeQuery(MediaItem mediaItem, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(mediaItem.title(), delim));
        builder.append(getEpisodeSearchComponent(mediaItem, delim));
        return builder.toString();
    }

    private String replaceSeparators(String title, String delim) {
        return String.join(delim, StringUtils.split(title, ":,;./\\ "));
    }

    private String getSeasonSearchComponent(MediaItem mediaItem) {
        return getShowDisplayForm(SEASON_PREFIX, mediaItem.season());
    }

    private String getEpisodeSearchComponent(MediaItem mediaItem, String delim) {
        StringBuilder builder = new StringBuilder(getSeasonSearchComponent(mediaItem));
        builder.append(delim);
        builder.append(getShowDisplayForm(EPISODE_PREFIX, mediaItem.episode()));
        return builder.toString();
    }

    private String getShowDisplayForm(String prefix, Integer unitNumber) {
        if (0 >= unitNumber) {
            throw new IllegalArgumentException("Show display unit cannot be lower than 1");
        }

        StringBuilder stringBuilder = new StringBuilder(prefix);
        if (unitNumber < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(unitNumber);
        return stringBuilder.toString();
    }
}
