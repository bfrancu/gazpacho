package gazpacho.core.persistence.match;

import gazpacho.core.match.MediaMatcher;
import gazpacho.core.model.MediaRelease;
import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.persistence.service.ReleaseService;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Priority(10)
@Decorator
@RequiredArgsConstructor
public class PersistenceMediaMatcher implements MediaMatcher {
    @Inject
    @Any
    @Delegate
    MediaMatcher delegate;

    @Inject
    ReleaseService releaseService;

    @Override
    public boolean match(@NonNull VisualMedia visualMedia) {
        boolean sourceMatch = delegate.match(visualMedia);
        Optional<MediaRelease> optionalMediaRelease = releaseService.findMediaItemRelease(visualMedia.release());

        if (!sourceMatch && optionalMediaRelease.isPresent()) {
            reconcilePersistenceMetadata(visualMedia.release(), optionalMediaRelease.get());
        }

        return sourceMatch;
    }

    /**
     *
     * Excepting the case where we matched a TV episode against a persistent TV season, set the inLibrary flag
     * of the persistence release to false
     * @param matchedRelease
     * @param persistenceRelease
     */
    private void reconcilePersistenceMetadata(MediaRelease matchedRelease, MediaRelease persistenceRelease) {
        if (!(MediaReleaseType.TV_EPISODE.equals(matchedRelease.mediaReleaseType())
                && MediaReleaseType.TV_SEASON.equals(persistenceRelease.mediaReleaseType()))) {
            releaseService.updateRelease(persistenceRelease.toBuilder()
                    .inLibrary(false)
                    .build()
            );
        }
    }
}
