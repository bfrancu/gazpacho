package gazpacho.core.persistence.repository;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.MediaType;
import gazpacho.core.persistence.PersistenceUtilities;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.model.Release;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ReleaseRepositoryTest {

    @Inject
    ReleaseRepository releaseRepository;
    @Inject
    MediaItemRepository mediaItemRepository;

    @Test
    void getReleaseById_returnsEmptyWhenNotPresent() {
        Optional<Release> found = releaseRepository.getReleaseById(-1L);
        assertTrue(found.isEmpty());
    }

    @Test
    void getReleaseById_returnsPersistedRelease() {
        Release saved = persistRelease();
        Optional<Release> found = releaseRepository.getReleaseById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(MediaReleaseType.MOVIE, found.get().getReleaseType());
        assertNotNull(found.get().getItem());
        assertEquals(saved.getItem().getId(), found.get().getItem().getId());
    }

    private Release persistRelease() {
        MediaItem item = mediaItemRepository.save(MediaItem.builder()
                .id(MediaItemId.builder()
                        .tmdbId((long) PersistenceUtilities.randomInt(1, 10000))
                        .mediaType(MediaType.MOVIE)
                        .build())
                .title("Test Movie")
                .description("used in unit tests")
                .language("EN")
                .firstAirDate(LocalDate.now())
                .lastAirDate(LocalDate.now())
                .build());

        Release release = Release.builder()
                .releaseType(MediaReleaseType.MOVIE)
                .item(item)
                .inLibrary(true)
                .releaseDate(LocalDate.now())
                .build();

        return releaseRepository.save(release);
    }

}