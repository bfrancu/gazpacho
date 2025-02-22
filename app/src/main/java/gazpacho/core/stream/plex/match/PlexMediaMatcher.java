package gazpacho.core.stream.plex.match;

import gazpacho.core.match.MediaMatcher;
import gazpacho.core.model.MediaItem;
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
    public boolean match(@NonNull MediaItem mediaItem) {
        Optional<?> matchedValue = switch (mediaItem.mediaReleaseType()) {
            case MOVIE -> searchMovie(mediaItem);
            case TV_SEASON -> searchSeason(mediaItem);
            case TV_EPISODE -> searchEpisode(mediaItem);
            default -> throw new IllegalArgumentException(
                    String.format("Media type %s not supported", mediaItem.mediaReleaseType()));
        };
        return matchedValue.isPresent();
    }

    public Optional<Movie> searchMovie(MediaItem mediaItem) {
        if (!mediaItem.isMovie()) {
            return Optional.empty();
        }

        for (Library library : mediaTypeLibraryMap.getOrDefault(MediaReleaseType.MOVIE, List.of())) {
            SearchResponse response = plexClient.search(
                    library.sectionId(),
                    mediaItem.title(),
                    SearchScope.MOVIE
            );

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (StringUtils.isNotBlank(metadata.title()) && mediaItem.title().equals(metadata.title())) {
                    return Optional.of(Movie.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Show> searchShow(MediaItem mediaItem) {
        if (mediaItem.isMovie()) {
            return Optional.empty();
        }

        for (Library library : mediaTypeLibraryMap.getOrDefault(MediaReleaseType.TV_SHOW, List.of())) {
            SearchResponse response = plexClient.search(
                    library.sectionId(),
                    mediaItem.title(),
                    SearchScope.TV_SHOW
            );

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (StringUtils.isNotBlank(metadata.title()) && mediaItem.title().equals(metadata.title())) {
                    return Optional.of(Show.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Season> searchSeason(MediaItem mediaItem) {
        Optional<Show> show = searchShow(mediaItem);

        if (show.isPresent()) {
            GetChildrenResponse response = plexClient.getChildren(show.get().ratingKey());

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (MediaReleaseType.TV_SEASON.equals(metadata.type())
                        && mediaItem.season().equals(metadata.index())) {
                    return Optional.of(Season.fromMetadata(metadata));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Episode> searchEpisode(MediaItem mediaItem) {
        Optional<Season> season = searchSeason(mediaItem);

        if (season.isPresent()) {
            GetChildrenResponse response = plexClient.getChildren(season.get().ratingKey());

            for (Metadata metadata : response.mediaContainer().metadata()) {
                if (MediaReleaseType.TV_EPISODE.equals(metadata.type())
                        && mediaItem.episode().equals(metadata.index())) {
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