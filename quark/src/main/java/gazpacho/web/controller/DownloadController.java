package gazpacho.web.controller;

import gazpacho.core.datasource.MediaDataSourceRetriever;
import gazpacho.core.identify.MediaIdentifier;
import gazpacho.core.match.MediaMatcher;
import gazpacho.core.model.MediaRelease;
import gazpacho.core.model.RequestStatus;
import gazpacho.core.model.UserProfile;
import gazpacho.core.model.UserRequest;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.model.DataSource;
import gazpacho.core.model.exchange.DownloadNotification;
import gazpacho.core.model.exchange.DownloadNotificationResponse;
import gazpacho.core.model.exchange.RequestFailure;
import gazpacho.core.model.exchange.NotificationRequestStatus;
import gazpacho.core.persistence.service.DataSourceService;
import gazpacho.core.persistence.service.MediaItemService;
import gazpacho.core.persistence.service.ProfileService;
import gazpacho.core.persistence.service.ReleaseService;
import gazpacho.core.persistence.service.RequestService;
import gazpacho.identify.inject.qualifiers.tmdb.ExternalIdentifier;
import gazpacho.identify.inject.qualifiers.tmdb.TmdbIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class DownloadController {
    private static final String NO_MEDIA_FOUND_FORMAT = "No media found for identifier: %s";
    private static final String NO_DATA_SOURCE_FOUND_FORMAT = "No torrent found for media item %s";
    private static final String MEDIA_FOUND_FORMAT = "File %s with size %s will be queued for download";

    @Inject
    ProfileService profileService;

    @Inject
    MediaItemService mediaItemService;

    @Inject
    RequestService requestService;

    @Inject
    ReleaseService releaseService;

    @Inject
    DataSourceService dataSourceService;

    @Inject
    @TmdbIdentifier
    MediaIdentifier tmdbMediaIdentifier;

    @Inject
    @ExternalIdentifier
    MediaIdentifier externalMediaIdentifier;

    @Inject
    MediaMatcher mediaMatcher;

    @Inject
    MediaDataSourceRetriever mediaDataSourceRetriever;

    public DownloadNotificationResponse downloadWithExternalIdentifier(DownloadNotification notification) {
        return download(notification, externalMediaIdentifier);
    }

    public DownloadNotificationResponse downloadWithTmdbIdentifier(DownloadNotification notification) {
        return download(notification, tmdbMediaIdentifier);
    }

    /*
    1. create user profile if it does not exist ---done
    2. identify media item by passed identifier ---done
    3. if media item is not found, return failure response ---done
    4. if media item is found, add it to persistence if it does not exist ---done

    5. check if media item is already downloaded or in progress
       match against a. the plex library (source of truth)
                     b. the database (local source of truth)
       reconcile the two sources of truth if applicable (e.g. if the media item is in the database but not in plex)
        ---done

    6. if media item has already an associated request, return success response ---done
    7. if media item is not downloaded persist a new internal request and release for the user request
    8. search for the media source
    9. if media source is found, create a new media data source persistence object associated with request,
       update request status
    10. if media source is not found, update request status and return failure
    11. respond back to the user asking for confirmation
    12. user confirms, send request persistenceId to the download queue, update request status
     */
    private DownloadNotificationResponse download(DownloadNotification notification, MediaIdentifier mediaIdentifier) {
        profileService.persist(notification.userProfile());
        Optional<VisualMedia> identifiedMedia = mediaIdentifier.identify(notification.payload().identifier());
        if (identifiedMedia.isEmpty()) {
            return createFailureResponse(String.format(NO_MEDIA_FOUND_FORMAT, notification.payload().identifier()));
        }

        VisualMedia visualMedia = identifiedMedia.get();

        mediaItemService.persist(visualMedia.metadata());
        Optional<DownloadNotificationResponse> matchedResponse = matchExistingMediaRequest(notification.payload().identifier(), visualMedia);
        if (matchedResponse.isPresent()) {
            return matchedResponse.get();
        }

        UserRequest persistedRequest = requestService.persist(UserRequest.builder()
                .mediaRelease(releaseService.persist(visualMedia.release()))
                .userProfile(notification.userProfile())
                .requestStatus(RequestStatus.PENDING)
                .query(notification.payload().identifier())
                .build());

        Optional<DataSource> dataSource = mediaDataSourceRetriever.retrieveDataSource(visualMedia);
        if (dataSource.isPresent()) {
            DataSource persistedDataSource = dataSourceService.persist(dataSource.get().toBuilder()
                    .userRequest(persistedRequest)
                    .build());

            requestService.updateRequest(persistedRequest.toBuilder()
                    .requestStatus(RequestStatus.QUEUED_FOR_DOWNLOAD)
                    .build());

            return createSuccessResponse(String.format(MEDIA_FOUND_FORMAT, persistedDataSource.name(), persistedDataSource.size()));
        }

        requestService.updateRequest(persistedRequest.toBuilder()
                .requestStatus(RequestStatus.DATA_SOURCE_NOT_FOUND)
                .build());

        return createFailureResponse(String.format(NO_DATA_SOURCE_FOUND_FORMAT, visualMedia.metadata().title()));
    }

    private Optional<DownloadNotificationResponse> matchExistingMediaRequest(String requestQuery, VisualMedia identifiedMedia) {
        if (mediaMatcher.match(identifiedMedia)) {
            return Optional.of(createSuccessResponse("Item already exists in the plex library"));
        }

        if (requestService.existsWithQuery(requestQuery)) {
            return Optional.of(createSuccessResponse(
                    String.format("Request already exists for the same query %s", requestQuery)));
        }

        return Optional.empty();
    }

    private DownloadNotificationResponse createSuccessResponse(String message) {
        return DownloadNotificationResponse.builder()
                .status(NotificationRequestStatus.SUCCESS)
                .message(message)
                .build();
    }

    private DownloadNotificationResponse createFailureResponse(String reason) {
        return DownloadNotificationResponse.builder()
                .status(NotificationRequestStatus.FAILURE)
                .requestFailure(RequestFailure.builder()
                        .reason(reason)
                        .build())
                .build();
    }
}
