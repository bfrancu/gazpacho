package gazpacho.core.persistence.repository;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.MediaType;
import gazpacho.core.model.RequestStatus;
import gazpacho.core.persistence.PersistenceUtilities;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.model.Profile;
import gazpacho.core.persistence.model.Release;
import gazpacho.core.persistence.model.Request;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class RequestRepositoryTest {

    @Inject
    RequestRepository requestRepository;
    @Inject
    ProfileRepository profileRepository;
    @Inject
    ReleaseRepository releaseRepository;
    @Inject
    MediaItemRepository mediaItemRepository;

    @Test
    void findAllByQuery_returnsEmptyListWhenNoMatch() {
        List<Request> found = requestRepository.findAllByQuery("no-such-query");
        assertTrue(found.isEmpty());
    }

    @Test
    void findById_returnsEmptyWhenNotPresent() {
        Optional<Request> found = requestRepository.findById(-1L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAllByQuery_returnsMatchingRequest() {
        Request saved = persistRequest("happy-query");
        List<Request> found = requestRepository.findAllByQuery("happy-query");
        assertEquals(1, found.size());
        assertEquals(saved.getId(), found.get(0).getId());
        assertEquals("happy-query", found.get(0).getQuery());
        Release release = found.get(0).getRelease();
        assertEquals(MediaReleaseType.MOVIE, release.getReleaseType());
    }

    @Test
    void findById_returnsPersistedRequest() {
        Request saved = persistRequest("find-by-id-happy");
        Optional<Request> found = requestRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("find-by-id-happy", found.get().getQuery());
    }

    private Request persistRequest(String query) {
        Profile profile = profileRepository.save(Profile.builder()
                .userId(String.format("user%d", PersistenceUtilities.randomInt(1, 1000)))
                .build());

        MediaItem item = mediaItemRepository.save(MediaItem.builder()
                .id(MediaItemId.builder()
                        .tmdbId((long)  PersistenceUtilities.randomInt(1, 10000))
                        .mediaType(MediaType.MOVIE)
                        .build())
                .title("Test Movie")
                .description("used in unit tests")
                .language("EN")
                .firstAirDate(LocalDate.now())
                .lastAirDate(LocalDate.now())
                .build());

        Release release = releaseRepository.save(Release.builder()
                .releaseType(MediaReleaseType.MOVIE)
                .item(item)
                .inLibrary(true)
                .releaseDate(LocalDate.now())
                .build());

        Request request = Request.builder()
                .originator(profile)
                .item(item)
                .release(release)
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .query(query)
                .build();

        return requestRepository.save(request);
    }
}