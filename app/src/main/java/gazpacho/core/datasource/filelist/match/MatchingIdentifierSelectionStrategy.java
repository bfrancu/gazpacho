package gazpacho.core.datasource.filelist.match;

import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.model.MediaReleaseType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MatchingIdentifierSelectionStrategy implements SearchResultSelectionStrategy {
    private final Comparator<SearchResultEntry> entryComparator;
    private final ItemQueryConverter itemQueryConverter;
    private final Logger logger;

    public MatchingIdentifierSelectionStrategy(@NonNull ItemQueryConverter itemQueryConverter,
                                               @NonNull Comparator<SearchResultEntry> entryComparator,
                                               @NonNull Logger logger) {
        this.itemQueryConverter = itemQueryConverter;
        this.entryComparator = entryComparator;
        this.logger = logger;
    }

    @Override
    public Optional<SearchResultEntry> select(@NonNull List<SearchResultEntry> resultEntries,
                                              @NonNull VisualMedia visualMedia) {
        logger.info("Selecting the best match for {} from {}", visualMedia, resultEntries);
        if (resultEntries.isEmpty()) {
            return Optional.empty();
        }

        List<SearchResultEntry> matched = filterByExactMatch(resultEntries, visualMedia);
        if (matched.isEmpty()) {
            return Optional.empty();
        }

        PriorityQueue<SearchResultEntry> priorityQueue = new PriorityQueue<>(matched.size(), this.entryComparator);
        matched.forEach(priorityQueue::add);

        return Optional.of(priorityQueue.poll());
    }

    private List<SearchResultEntry> filterByExactMatch(List<SearchResultEntry> resultEntries,
                                                       VisualMedia visualMedia) {
        String searchQueryValue = itemQueryConverter.getSearchResultTitle(visualMedia);
        logger.info("Matching by title prefix {}", searchQueryValue);
        List<SearchResultEntry> matched = resultEntries.stream()
                .filter(result -> result.title()
                        .toLowerCase(Locale.ROOT)
                        .startsWith(searchQueryValue.toLowerCase(Locale.ROOT)))
                .filter(result -> genreMatches(result, visualMedia))
                .collect(Collectors.toList());

        if (matched.isEmpty() && visualMedia.isEpisode()) {
            VisualMedia seasonVisualMedia = visualMedia.toBuilder()
                    .release(visualMedia.release()
                            .toBuilder()
                            .mediaReleaseType(MediaReleaseType.TV_SEASON)
                            .build())
                    .build();
            return filterByExactMatch(resultEntries, seasonVisualMedia);
        }

        return matched;
    }

    private boolean genreMatches(SearchResultEntry resultEntry, VisualMedia visualMedia) {
        logger.info("Genre matches {} : {}", resultEntry, visualMedia);
        return switch (visualMedia.release().mediaReleaseType()) {
            case MediaReleaseType.MOVIE -> resultEntry.category().category().isMovie();
            case MediaReleaseType.TV_EPISODE,
                 MediaReleaseType.TV_SEASON -> resultEntry.category().category().isTv();
            default -> false;
        };
    }
}
