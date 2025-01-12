package gazpacho.core.datasource.filelist.navigate;

import gazpacho.core.datasource.filelist.match.ItemQueryConverter;
import gazpacho.core.datasource.filelist.model.MediaCategory;
import gazpacho.core.model.MediaItem;
import lombok.NonNull;

import java.util.Map;

public class QueryUrlResolver {
    private static final String BROWSE_BASE_URL = "https://filelist.io/browse.php";
    private static final String QUERY_DELIM = "?";
    private static final String QUERY_ARG_SEP = "&";
    private static final String QUERY_KEY_VALUE_SEP = "=";
    private static final String QUERY_SEARCH_KEY = "search";
    private static final String QUERY_CATEGORY_KEY = "cat";
    private static final String QUERY_SEARCH_IN_KEY = "searchin";
    private static final String QUERY_SEARCH_IN_NAME = "1";
    private static final String QUERY_SORT_KEY = "sort";
    private static final String QUERY_SORT_BY_DATA = "2";

    private final ItemQueryConverter itemQueryConverter;

    public QueryUrlResolver(@NonNull ItemQueryConverter itemQueryConverter) {
        this.itemQueryConverter = itemQueryConverter;
    }

    public String getQueryUrl(@NonNull MediaItem mediaItem) {
        String searchQueryValue = itemQueryConverter.getQuerySearchValue((mediaItem));
        return getUrl(searchQueryValue);
    }

    private String getUrl(String searchQuery) {
        StringBuilder builder = new StringBuilder(BROWSE_BASE_URL);
        builder.append(QUERY_DELIM);
        builder.append(getQueryParameters(Map.of(
                QUERY_SEARCH_KEY, searchQuery,
                QUERY_CATEGORY_KEY, MediaCategory.ALL.value().toString(),
                QUERY_SEARCH_IN_KEY, QUERY_SEARCH_IN_NAME,
                QUERY_SORT_KEY, QUERY_SORT_BY_DATA
        )));
        return builder.toString();
    }

    private String getQueryParameters(Map<String, String> keyValueMap) {
        StringBuilder builder = new StringBuilder();
        for (var entry : keyValueMap.entrySet()) {
            String queryParam = getQueryParam(entry.getKey(), entry.getValue());
            if (!builder.isEmpty()) {
                builder.append(QUERY_ARG_SEP);
            }
            builder.append(queryParam);
        }
        return builder.toString();
    }


    private String getQueryParam(String key, String value) {
        return key + QUERY_KEY_VALUE_SEP + value;
    }

}
