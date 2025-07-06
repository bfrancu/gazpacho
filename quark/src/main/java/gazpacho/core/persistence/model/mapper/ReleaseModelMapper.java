package gazpacho.core.persistence.model.mapper;

import gazpacho.core.model.MediaRelease;
import gazpacho.core.persistence.model.Release;
import gazpacho.core.persistence.service.EntityFetcherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReleaseModelMapper implements PersistenceModelMapper<MediaRelease, Release> {
    @Inject
    EntityFetcherService entityFetcherService;

    @Override
    public Release toPersistenceModel(MediaRelease mediaRelease) {
        if (mediaRelease.hasPersistenceId()) {
            return entityFetcherService.fetchRelease(mediaRelease);
        }

        return Release.builder()
                .item(entityFetcherService.fetchMediaItem(mediaRelease.mediaId()))
                .releaseDate(mediaRelease.releaseDate())
                .releaseType(mediaRelease.mediaReleaseType())
                .inLibrary(false)
                .season(mediaRelease.season())
                .episode(mediaRelease.episode())
                .build();
    }

    @Override
    public MediaRelease fromPersistenceModel(Release release) {
        return MediaRelease.builder()
                .persistenceId(release.getId())
                .releaseDate(release.getReleaseDate())
                .mediaReleaseType(release.getReleaseType())
                .season(release.getSeason())
                .episode(release.getEpisode())
                .inLibrary(release.isInLibrary())
                .build();
    }
}
