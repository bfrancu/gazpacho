package gazpacho.core.persistence.repository;

import gazpacho.core.persistence.PersistenceUtilities;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.model.MediaItemSubscription;
import gazpacho.core.model.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MediaItemSubscriptionRepositoryTest {

    @Inject
    MediaItemSubscriptionRepository mediaItemSubscriptionRepository;
    @Inject
    MediaItemRepository mediaItemRepository;

    @Test
    void getSubscriptionById_returnsEmptyWhenNotPresent() {
        Optional<MediaItemSubscription> found = mediaItemSubscriptionRepository.getSubscriptionById(-1L);
        assertTrue(found.isEmpty());
    }

    @Test
    void getSubscriptionById_returnsPersistedSubscription() {
        MediaItemSubscription saved = persistSubscription();
        Optional<MediaItemSubscription> found = mediaItemSubscriptionRepository.getSubscriptionById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertNotNull(found.get().getItem());
        assertEquals(saved.getItem().getId(), found.get().getItem().getId());
    }

    private MediaItemSubscription persistSubscription() {
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

        MediaItemSubscription subscription = MediaItemSubscription.builder()
                .item(item)
                .lastScanned(LocalDate.now().minusDays(1))
                .build();

        return mediaItemSubscriptionRepository.save(subscription);
    }
}
