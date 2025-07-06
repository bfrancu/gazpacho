package gazpacho.core.persistence.repository;

import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.model.MediaType;
import gazpacho.core.model.RequestStatus;
import gazpacho.core.model.SizeUnit;
import gazpacho.core.persistence.PersistenceUtilities;
import gazpacho.core.persistence.model.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MediaDataSourceRepositoryTest {

    @Inject
    MediaDataSourceRepository mediaDataSourceRepository;
    @Inject
    MediaItemRepository mediaItemRepository;
    @Inject
    ReleaseRepository releaseRepository;
    @Inject
    ProfileRepository profileRepository;
    @Inject
    RequestRepository requestRepository;

    @Test
    void findWithRequestGraphById_returnsEmptyWhenNotPresent() {
        Optional<MediaDataSource> found = mediaDataSourceRepository.findWithRequestGraphById(-1L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findWithRequestGraphById_returnsPersistedDataSource() {
        MediaDataSource saved = persistDataSource();
        Optional<MediaDataSource> found = mediaDataSourceRepository.findWithRequestGraphById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertNotNull(found.get().getRequest());
        assertNotNull(found.get().getTorrentHash());
        assertNotNull(found.get().getTorrentName());
        assertNotNull(found.get().getDownloadLocation());
        assertNotNull(found.get().getFinished());
        assertNotNull(found.get().getMediaSize());
    }

    private MediaDataSource persistDataSource() {
        // Persist required Profile
        Profile profile = profileRepository.save(Profile.builder()
                .userId("user" + PersistenceUtilities.randomInt(1, 1000))
                .build());

        // Persist required MediaItem
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

        // Persist required Release
        Release release = releaseRepository.save(Release.builder()
                .releaseType(MediaReleaseType.MOVIE)
                .item(item)
                .inLibrary(true)
                .releaseDate(LocalDate.now())
                .build());

        // Persist required Request
        Request request = requestRepository.save(Request.builder()
                .originator(profile)
                .item(item)
                .release(release)
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .query("datasource-test-query")
                .build());

        // Build and persist MediaDataSource
        MediaDataSource dataSource = MediaDataSource.builder()
                .request(request)
                .torrentHash("hash-" + PersistenceUtilities.randomInt(1, 10000))
                .torrentName("torrent-file-" + PersistenceUtilities.randomInt(1, 10000))
                .downloadLocation("/tmp/downloads/" + PersistenceUtilities.randomInt(1, 10000))
                .finished(Boolean.FALSE)
                .mediaSize(MediaSize.builder().size(2.0).unit(SizeUnit.GB).build())
                .build();

        return mediaDataSourceRepository.save(dataSource);
    }
}