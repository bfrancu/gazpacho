package gazpacho.core.stream.plex.match;

import gazpacho.core.match.MediaMatcher;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.model.MediaReleaseType;
import gazpacho.core.stream.plex.PlexClient;
import gazpacho.core.stream.plex.model.*;
import gazpacho.core.stream.plex.model.io.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PlexMediaMatcher implements MediaMatcher {

    private final PlexClient plexClient;
    private Map<MediaReleaseType, List<Library>> mediaTypeLibraryMap;

    public PlexMediaMatcher(@NonNull PlexClient plexClient) {
        this.plexClient = plexClient;
        this.mediaTypeLibraryMap = getPlexMediaLibraries();
    }

    @Override
    public boolean match(@NonNull VisualMedia visualMedia) {
        Optional<?> matchedValue = switch (visualMedia.release().mediaReleaseType()) {
            case MOVIE -> searchMovie(visualMedia);
            case TV_SEASON -> searchSeason(visualMedia);
            case TV_EPISODE -> searchEpisode(visualMedia);
            default -> throw new IllegalArgumentException(
                    String.format("Media type %s not supported", visualMedia.release().mediaReleaseType()));
        };
        return matchedValue.isPresent();
    }

    public Optional<Movie> searchMovie(VisualMedia visualMedia) {
        if (!visualMedia.isMovie()) {
            return Optional.empty();
        }

        for (Library library : mediaTypeLibraryMap.getOrDefault(MediaReleaseType.MOVIE, List.of())) {
            SearchResponse response = plexClient.search(
                    library.sectionId(),
                    visualMedia.metadata().title(),
                    SearchScope.MOVIE
            );

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (StringUtils.isNotBlank(metadata.title())
                        && visualMedia.metadata().title().equals(metadata.title())) {
                    return Optional.of(Movie.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Show> searchShow(VisualMedia visualMedia) {
        if (visualMedia.isMovie()) {
            return Optional.empty();
        }

        for (Library library : mediaTypeLibraryMap.getOrDefault(MediaReleaseType.TV_SHOW, List.of())) {
            SearchResponse response = plexClient.search(
                    library.sectionId(),
                    visualMedia.metadata().title(),
                    SearchScope.TV_SHOW
            );

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (StringUtils.isNotBlank(metadata.title())
                        && visualMedia.metadata().title().equals(metadata.title())) {
                    return Optional.of(Show.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Season> searchSeason(VisualMedia visualMedia) {
        Optional<Show> show = searchShow(visualMedia);

        if (show.isPresent()) {
            GetChildrenResponse response = plexClient.getChildren(show.get().ratingKey());

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (MediaReleaseType.TV_SEASON.equals(metadata.type())
                        && visualMedia.release().season().equals(metadata.index())) {
                    return Optional.of(Season.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Episode> searchEpisode(VisualMedia visualMedia) {
        Optional<Season> season = searchSeason(visualMedia);

        if (season.isPresent()) {
            GetChildrenResponse response = plexClient.getChildren(season.get().ratingKey());

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (MediaReleaseType.TV_EPISODE.equals(metadata.type())
                        && visualMedia.release().episode().equals(metadata.index())) {
                    return Optional.of(Episode.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    private Map<MediaReleaseType, List<Library>> getPlexMediaLibraries() {
        GetAllLibrariesResponse response = plexClient.getLibraries();
        List<Directory> directoryList = response.mediaContainer().directories();

        Map<MediaReleaseType, List<Library>> result = new HashMap<>();

        if (null != directoryList) {
            directoryList.forEach(directory -> {
               result.putIfAbsent(directory.type(), new ArrayList<>());
               result.get(directory.type()).add(Library.fromDirectory(directory));
            });
        }
        return result;
    }
}