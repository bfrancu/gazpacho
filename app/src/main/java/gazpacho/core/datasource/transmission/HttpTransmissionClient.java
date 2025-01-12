package gazpacho.core.datasource.transmission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import gazpacho.core.datasource.transmission.model.io.*;
import gazpacho.core.datasource.transmission.model.io.GetRequest.Arguments;
import gazpacho.core.exception.NonRetryableException;
import gazpacho.core.exception.RetryableException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HttpTransmissionClient implements TransmissionClient {

    private static final String HTTP_PROTOCOL = "http://";
    private static final String PORT_SEP = ":";
    private static final String URI_TRANSMISSION_PATH = "/transmission/rpc";
    private static final String TRANSMISSION_SESSION_ID_HEADER = "X-Transmission-Session-Id";
    private static final String CONTENT_TYPE_HEADER = "Content-type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String GET_METHOD = "torrent-get";
    private static final String ADD_METHOD = "torrent-add";
    private static final String DEL_METHOD = "torrent-remove";

    private static final List<String> GET_FIELD_NAMES = Arrays.asList(
            Torrent.class.getDeclaredFields()).stream()
            .map(Field::getName)
            .collect(Collectors.toList());

    private static final Arguments GET_REQUEST_ARGUMENTS = Arguments.builder()
            .fields(GET_FIELD_NAMES)
            .build();

    private Integer tag = 0;
    private String sessionId = "";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final Integer port;
    private final String host;

    @Override
    public TorrentAdded download(@NonNull Path dataSourcePath, @NonNull Path downloadPath) {
        logger.info("Download data source {} to {}", dataSourcePath, downloadPath);
        AddResponse.Arguments addResponse = sendRequest(
                AddRequest.builder()
                        .method(ADD_METHOD)
                        .tag(tag)
                        .arguments(AddRequest.Arguments.builder()
                                .downloadDir(downloadPath.toString())
                                .filename(dataSourcePath.toString())
                                .build())
                        .build(),
                AddResponse.class);

        if (null != addResponse.torrentAdded()) {
            return addResponse.torrentAdded();
        }

        if (null == addResponse.torrentDuplicate()){
            throw new RetryableException("No torrent information available in the response");
        }

        return addResponse.torrentDuplicate();
    }

    @Override
    public void remove(@NonNull List<Integer> ids, boolean deleteLocalData) {
        logger.info("Remove {}", ids);
        sendRequest(
          RemoveRequest.builder()
                  .method(DEL_METHOD)
                  .tag(tag)
                  .arguments(RemoveRequest.Arguments.builder()
                          .ids(ids)
                          .deleteLocalData(deleteLocalData)
                          .build())
                  .build(),
                RemoveResponse.class
        );
    }

    @Override
    public ImmutableList<Torrent> listActive() {
        logger.info("List active");
        GetResponse.Arguments getResponse = sendRequest(
                GetRequest.builder()
                        .method(GET_METHOD)
                        .tag(tag)
                        .arguments(GET_REQUEST_ARGUMENTS)
                        .build(),
                GetResponse.class
        );

        return getResponse.torrents();
    }

    private <RequestArguments, ResponseArguments, R extends Response<ResponseArguments>>
    ResponseArguments sendRequest(Request<RequestArguments> request, Class<R> responseClass) {
        try {
            return httpClient.execute(getPostMethod(request), response -> {
                try {
                    if (HttpStatus.SC_CONFLICT == response.getCode()) {
                        sessionId = response.getHeader(TRANSMISSION_SESSION_ID_HEADER).getValue();
                        return sendRequest(request, responseClass);
                    } else if (HttpStatus.SC_SUCCESS == response.getCode()) {
                        String responseBuffer = EntityUtils.toString(response.getEntity());
                        R responseDao = objectMapper.readValue(responseBuffer, responseClass);
                        tag = responseDao.getTag() + 1;
                        return responseDao.getArguments();
                    }
                    throw new NonRetryableException("Invalid response");
                } catch (ProtocolException | RuntimeException e) {
                    throw new NonRetryableException("Runtime exception encountered", e);
                } finally {
                    response.close();
                }
            });
        } catch (IOException e) {
            throw new RetryableException(e.getMessage(), e);
        }
    }

    private <RequestArguments> HttpPost getPostMethod(Request<RequestArguments> request) {
        HttpPost httpPost = new HttpPost(getRequestUri());
        httpPost.addHeader(TRANSMISSION_SESSION_ID_HEADER, sessionId);
        httpPost.addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);

        try {
            StringEntity postBody = new StringEntity(objectMapper.writeValueAsString(request));
            httpPost.setEntity(postBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return httpPost;
    }

    private String getRequestUri() {
        return HTTP_PROTOCOL +
                host + PORT_SEP + port +
                URI_TRANSMISSION_PATH;
    }
}