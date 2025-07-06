package gazpacho.core.persistence.service;

import gazpacho.core.model.MediaRelease;
import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.Release;
import gazpacho.core.persistence.model.mapper.ReleaseModelMapper;
import gazpacho.core.persistence.repository.ReleaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ReleaseService {
    @Inject
    ReleaseRepository releaseRepository;

    @Inject
    EntityFetcherService entityFetcherService;

    @Inject
    ReleaseModelMapper releaseModelMapper;

    @Transactional
    public MediaRelease updateRelease(MediaRelease mediaRelease) {
        if (mediaRelease.hasPersistenceId()) {
            Release release = entityFetcherService.fetchRelease(mediaRelease);
            release.setInLibrary(mediaRelease.inLibrary());
            return fromPersistence(releaseRepository.save(release));
        }
        return mediaRelease;
    }

    @Transactional
    public MediaRelease persist(@NonNull MediaRelease mediaRelease) {
        if (mediaRelease.hasPersistenceId()) {
            return updateRelease(mediaRelease);
        }

        return fromPersistence(releaseRepository.save(toPersistence(mediaRelease)));
    }

    private Release toPersistence(MediaRelease mediaRelease) {
        return releaseModelMapper.toPersistenceModel(mediaRelease);
    }

    private MediaRelease fromPersistence(Release release) {
        return releaseModelMapper.fromPersistenceModel(release);
    }

    @Transactional(readOnly = true)
    public Optional<MediaRelease> findMediaItemRelease(MediaRelease mediaRelease) {
        Optional<MediaItem> optionalMediaItem =
                entityFetcherService.findMediaItemWithReleases(mediaRelease.mediaId());

        if(optionalMediaItem.isPresent()) {
            return searchReleaseInMediaItem(optionalMediaItem.get().getReleases(), mediaRelease);
        }

        return Optional.empty();
    }

    private Optional<MediaRelease> searchReleaseInMediaItem(Set<Release> mediaItemReleases, MediaRelease mediaRelease) {
        Optional<Release> optionalRelease = switch (mediaRelease.mediaReleaseType()) {
            case MediaReleaseType.MOVIE -> getMovieRelease(mediaItemReleases);
            case MediaReleaseType.TV_SEASON -> getTvSeasonRelease(mediaItemReleases, mediaRelease);
            case MediaReleaseType.TV_EPISODE -> getTvEpisodeRelease(mediaItemReleases, mediaRelease);
            default -> Optional.empty();
        };

        if (optionalRelease.isPresent()) {
            return Optional.of(fromPersistence(optionalRelease.get()));
        }

        return Optional.empty();
    }

    private Optional<Release> getMovieRelease(Set<Release> mediaItemReleases) {
        if (1 == mediaItemReleases.size()) {
            return Optional.ofNullable(mediaItemReleases.stream()
                    .filter(release -> MediaReleaseType.MOVIE.equals(release.getReleaseDate()) && release.isInLibrary())
                    .findFirst()
                    .orElse(null));
        }

        return Optional.empty();
    }

    private Optional<Release> getTvSeasonRelease(Set<Release> mediaItemReleases, MediaRelease mediaRelease) {
        for (Release r : mediaItemReleases) {
            if (MediaReleaseType.TV_SEASON.equals(r.getReleaseType())
                    && r.isInLibrary()
                    && r.getSeason() == mediaRelease.season()) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    private Optional<Release> getTvEpisodeRelease(Set<Release> mediaItemReleases, MediaRelease mediaRelease) {
        for (Release r : mediaItemReleases) {
            if (MediaReleaseType.TV_EPISODE.equals(r.getReleaseType())
                    && r.isInLibrary()
                    && matchIntegerValues(r.getSeason(), mediaRelease.season())
                    && matchIntegerValues(r.getEpisode(), mediaRelease.episode())) {
                return Optional.of(r);
            }
        }
        return getTvSeasonRelease(mediaItemReleases, mediaRelease);
    }

    boolean matchIntegerValues(Integer a, Integer b) {
        return null != a && null != b && a.equals(b);
    }
}
