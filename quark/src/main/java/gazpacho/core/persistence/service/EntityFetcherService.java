package gazpacho.core.persistence.service;

import gazpacho.core.model.MediaId;
import gazpacho.core.model.MediaMetadata;
import gazpacho.core.model.MediaRelease;
import gazpacho.core.persistence.model.MediaDataSource;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.Release;
import gazpacho.core.persistence.model.Request;
import gazpacho.core.persistence.repository.MediaDataSourceRepository;
import gazpacho.core.persistence.repository.MediaItemRepository;
import gazpacho.core.persistence.repository.ProfileRepository;
import gazpacho.core.persistence.repository.ReleaseRepository;
import gazpacho.core.persistence.repository.RequestRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class EntityFetcherService {
    @Inject
    private MediaItemRepository mediaItemRepository;

    @Inject
    private ReleaseRepository releaseRepository;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private MediaDataSourceRepository mediaDataSourceRepository;

    public Optional<Profile> findProfile(String userId) {
        return profileRepository.findById(userId);
    }

    public Optional<MediaItem> findMediaItem(MediaId mediaId) {
        return mediaItemRepository.findById(MediaItemId.fromMediaId(mediaId));
    }

    public Optional<MediaItem> findMediaItemWithReleases(MediaId mediaId) {
        return mediaItemRepository.getMediaItemWithReleasesById(MediaItemId.fromMediaId(mediaId));
    }

    public Optional<MediaDataSource> findMediaDataSource(Long dataSourceId) {
        return mediaDataSourceRepository.findById(dataSourceId);
    }

    public Optional<Release> findRelease(MediaRelease mediaRelease) {
        return releaseRepository.getReleaseById(mediaRelease.persistenceId());
    }

    public Optional<Request> findRequest(Long requestId) {
        return requestRepository.findById(requestId);
    }

    public Profile fetchProfile(String userId) {
        return findProfile(userId)
                .orElseThrow(() -> new IllegalStateException("User profile not found"));
    }

    public MediaItem fetchMediaItem(MediaId mediaId) {
        return findMediaItem(mediaId)
                .orElseThrow(() -> new IllegalStateException("Media item not found"));
    }

    public MediaItem fetchMediaItemWithReleases(MediaId mediaId) {
        return findMediaItemWithReleases(mediaId)
                .orElseThrow(() -> new IllegalStateException("Media item not found"));
    }

    public MediaDataSource fetchMediaDataSource(Long dataSourceId) {
        return findMediaDataSource(dataSourceId)
                .orElseThrow(() -> new IllegalStateException("Media data source not found"));
    }

    public Release fetchRelease(MediaRelease mediaRelease) {
        return findRelease(mediaRelease)
                .orElseThrow(() -> new IllegalStateException("Media release not found"));
    }

    public Request fetchRequest(Long requestId) {
        return findRequest(requestId)
                .orElseThrow(() -> new IllegalStateException("Request not found"));
    }

}
