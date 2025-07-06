package gazpacho.core.stream.plex;

import com.fasterxml.jackson.databind.ObjectMapper;
import gazpacho.core.exception.NonRetryableException;
import gazpacho.core.exception.RetryableException;
import gazpacho.core.model.Empty;
import gazpacho.core.stream.plex.model.*;
import gazpacho.core.stream.plex.model.io.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;

@RequiredArgsConstructor
public class HttpPlexClient implements PlexClient {
    private static final String HTTP_SCHEMA = "http";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_PLEX_TOKEN_KEY = "X-Plex-Token";
    private static final String SEARCH_SCOPE_PARAM_KEY = "type";
    private static final String SEARCH_QUERY_PARAM_KEY = "query";
    private static final String PATH_SEP = "/";
    private static final String LIBRARY_PATH_ROOT = "/library";
    private static final String SECTIONS_PATH_PART = "sections";
    private static final String METADATA_PATH_PART = "metadata";
    private static final String SEARCH_PATH_PART = "search";
    private static final String CHILDREN_PATH_PART = "children";
    private static final String REFRESH_PATH_PART = "refresh";
    private static final String ALL_TAG_PATH_PART = "all";
    private static final String PLEX_LIBRARIES_PATH = LIBRARY_PATH_ROOT + PATH_SEP + SECTIONS_PATH_PART;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final Integer port;
    private final String host;
    private final String plexToken;

    @Override
    public SearchResponse search(@NonNull String sectionId, @NonNull String query, @NonNull SearchScope scope) {
        return sendRequest(getSearchPath(sectionId),
                Map.of(SEARCH_SCOPE_PARAM_KEY, String.valueOf(scope.value()),
                        SEARCH_QUERY_PARAM_KEY, query),
                SearchResponse.class);
    }

    @Override
    public GetAllLibrariesResponse getLibraries() {
        return sendRequest(PLEX_LIBRARIES_PATH, GetAllLibrariesResponse.class);
    }

    @Override
    public GetLibraryItemsResponse getLibraryItems(@NonNull String sectionId) {
        return sendRequest(getLibraryItemsPath(sectionId), GetLibraryItemsResponse.class);
    }

    @Override
    public GetMetadataResponse getMetadata(@NonNull String ratingKey) {
        return sendRequest(getMetadataPath(ratingKey), GetMetadataResponse.class);
    }

    @Override
    public GetChildrenResponse getChildren(@NonNull String ratingKey) {
        return sendRequest(getChildrenPath(ratingKey), GetChildrenResponse.class);
    }

    @Override
    public void rescan(@NonNull String sectionId) {
        sendRequest(getScanPath(sectionId), Map.of(), Empty.class);
    }

    private <R> R sendRequest(String path, Class<R> responseClass) {
        return sendRequest(path, Map.of(), responseClass);
    }

    private <R> R sendRequest(String path, Map<String, String> parameters, Class<R> responseClass) {
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(HTTP_SCHEMA)
                .setHost(host)
                .setPort(port)
                .setPath(path)
                .setParameter(X_PLEX_TOKEN_KEY, plexToken);

        for (var entry : parameters.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }

        try {
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader(ACCEPT_HEADER, APPLICATION_JSON);
            return httpClient.execute(httpGet, response -> {
                try {
                    if (HttpStatus.SC_SUCCESS == response.getCode()) {
                        String responseBuffer = EntityUtils.toString(response.getEntity());
                        if (StringUtils.isNotBlank(responseBuffer) && !Empty.class.equals(responseClass)) {
                            R responseDao = objectMapper.readValue(responseBuffer, responseClass);
//                            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDao);
//                            logger.info(prettyJson);
                            return responseDao;
                        }
                    }
                    return null;
                } finally {
                    response.close();
                }
            });
        } catch (URISyntaxException e) {
            throw new NonRetryableException(String.format("Invalid uri %s", uriBuilder), e);
        } catch (IOException e) {
            throw new RetryableException(e);
        }
    }

    private String getMetadataPath(String ratingKey) {
        return Paths.get(LIBRARY_PATH_ROOT,
                        METADATA_PATH_PART,
                        ratingKey)
                .toString();
    }

    private String getScanPath(String sectionId) {
        return Paths.get(LIBRARY_PATH_ROOT,
                        SECTIONS_PATH_PART,
                        sectionId,
                        REFRESH_PATH_PART)
                .toString();
    }

    private String getChildrenPath(String ratingKey) {
        return Paths.get(LIBRARY_PATH_ROOT,
                        METADATA_PATH_PART,
                        ratingKey,
                        CHILDREN_PATH_PART)
                .toString();
    }

    private String getSearchPath(String sectionId) {
        return Paths.get(LIBRARY_PATH_ROOT,
                        SECTIONS_PATH_PART,
                        sectionId,
                        SEARCH_PATH_PART)
                .toString();
    }

    private String getLibraryItemsPath(String sectionId) {
        return Paths.get(LIBRARY_PATH_ROOT,
                        SECTIONS_PATH_PART,
                        sectionId,
                        ALL_TAG_PATH_PART)
                .toString();
    }
}
