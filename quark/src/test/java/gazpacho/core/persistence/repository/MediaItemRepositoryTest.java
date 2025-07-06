package gazpacho.core.persistence.repository;

import gazpacho.core.model.MediaType;
import gazpacho.core.persistence.PersistenceUtilities;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MediaItemRepositoryTest {

    @Inject
    MediaItemRepository mediaItemRepository;

    @Test
    void getMediaItemById_returnsEmptyWhenNotPresent() {
        MediaItemId id = MediaItemId.builder()
                .tmdbId(-1L)
                .mediaType(MediaType.MOVIE)
                .build();
        Optional<MediaItem> found = mediaItemRepository.getMediaItemById(id);
        assertTrue(found.isEmpty());
    }

    @Test
    void getMediaItemById_returnsPersistedItem() {
        MediaItem saved = persistMediaItem();
        Optional<MediaItem> found = mediaItemRepository.getMediaItemById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Movie", found.get().getTitle());
    }

    @Test
    void getMediaItemWithReleasesById_returnsPersistedItem() {
        MediaItem saved = persistMediaItem();
        Optional<MediaItem> found = mediaItemRepository.getMediaItemWithReleasesById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Movie", found.get().getTitle());
        // Releases will be empty unless separately persisted and linked
        assertNotNull(found.get().getReleases());
    }

    private MediaItem persistMediaItem() {
        MediaItemId id = MediaItemId.builder()
                .tmdbId((long) PersistenceUtilities.randomInt(1, 10000))
                .mediaType(MediaType.MOVIE)
                .build();
        MediaItem item = MediaItem.builder()
                .id(id)
                .title("Test Movie")
                .description("used in unit tests")
                .language("EN")
                .firstAirDate(LocalDate.now())
                .lastAirDate(LocalDate.now())
                .build();
        return mediaItemRepository.save(item);
    }
}