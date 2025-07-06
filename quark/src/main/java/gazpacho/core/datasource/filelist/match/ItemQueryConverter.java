package gazpacho.core.datasource.filelist.match;

import gazpacho.core.model.VisualMedia;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class ItemQueryConverter {
    private static final String QUERY_SEARCH_VALUE_DELIM = "+";
    private static final String SEARCH_RESULT_TITLE_DELIM = ".";
    private static final String EPISODE_PREFIX = "E";
    private static final String SEASON_PREFIX = "S";
    private static final String AND_CHAR_REPLACEMENT = "and";

    public String getQuerySearchValue(@NonNull VisualMedia visualMedia) {
        return getTextRepresentation(visualMedia, QUERY_SEARCH_VALUE_DELIM);
    }

    public String getSearchResultTitle(@NonNull VisualMedia visualMedia) {
        return getTextRepresentation(visualMedia, SEARCH_RESULT_TITLE_DELIM);
    }

    private String getTextRepresentation(VisualMedia visualMedia, String delim) {
        return (switch(visualMedia.release().mediaReleaseType()) {
            case MOVIE -> getMovieQuery(visualMedia, delim);
            case TV_SEASON -> getTvSeasonQuery(visualMedia, delim);
            case TV_EPISODE -> getTvEpisodeQuery(visualMedia, delim);
            case TV_SHOW,
                 UNKNOWN -> throw new IllegalArgumentException(
                         String.format("Invalid media item %s type %s", visualMedia, visualMedia.release().mediaReleaseType()));
        });
    }


    private String getMovieQuery(VisualMedia visualMedia, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(visualMedia.metadata().title(), delim));
        builder.append(delim)
                .append(visualMedia.metadata().firstAirDate().getYear());
        return builder.toString();
    }

    private String getTvSeasonQuery(VisualMedia visualMedia, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(visualMedia.metadata().title(), delim));
        builder.append(delim)
                .append(getSeasonSearchComponent(visualMedia));
        return builder.toString();
    }

    private String getTvEpisodeQuery(VisualMedia visualMedia, String delim) {
        StringBuilder builder = new StringBuilder(replaceSeparators(visualMedia.metadata().title(), delim));
        builder.append(getEpisodeSearchComponent(visualMedia, delim));
        return builder.toString();
    }

    private String replaceSeparators(String title, String delim) {
        return String.join(delim,
                StringUtils.split(replaceSpecialCharacters(title), ":,;./\\ "));
    }

    private String replaceSpecialCharacters(String title) {
        return title.replace("&", AND_CHAR_REPLACEMENT);
    }

    private String getSeasonSearchComponent(VisualMedia visualMedia) {
        return getShowDisplayForm(SEASON_PREFIX, visualMedia.release().season());
    }

    private String getEpisodeSearchComponent(VisualMedia visualMedia, String delim) {
        StringBuilder builder = new StringBuilder(getSeasonSearchComponent(visualMedia));
        builder.append(delim);
        builder.append(getShowDisplayForm(EPISODE_PREFIX, visualMedia.release().episode()));
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
