package gazpacho.core.download.filelist.match;

import gazpacho.core.download.filelist.model.SearchResultEntry;
import gazpacho.core.model.MediaItem;
import gazpacho.core.model.MediaType;
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
                                              @NonNull MediaItem mediaItem) {
        logger.info("Selecting the best match for {} from {}", mediaItem, resultEntries);
        if (resultEntries.isEmpty()) {
            return Optional.empty();
        }

        List<SearchResultEntry> matched = filterByExactMatch(resultEntries, mediaItem);
        if (matched.isEmpty()) {
            return Optional.empty();
        }

        PriorityQueue<SearchResultEntry> priorityQueue = new PriorityQueue<>(matched.size(), this.entryComparator);
        matched.forEach(priorityQueue::add);

        return Optional.of(priorityQueue.poll());
    }

    private List<SearchResultEntry> filterByExactMatch(List<SearchResultEntry> resultEntries,
                                                       MediaItem mediaItem) {
        String searchQueryValue = itemQueryConverter.getSearchResultTitle(mediaItem);
        logger.info("Matching be title prefix {}", searchQueryValue);
        List<SearchResultEntry> matched = resultEntries.stream()
                .filter(result -> result.title()
                        .toLowerCase(Locale.ROOT)
                        .startsWith(searchQueryValue.toLowerCase(Locale.ROOT)))
                .filter(result -> genreMatches(result, mediaItem))
                .collect(Collectors.toList());

        if (matched.isEmpty() && mediaItem.isEpisode()) {
            MediaItem seasonMediaItem = mediaItem.toBuilder()
                    .mediaType(MediaType.TV_SEASON)
                    .build();
            return filterByExactMatch(resultEntries, seasonMediaItem);
        }

        return matched;
    }

    private boolean genreMatches(SearchResultEntry resultEntry, MediaItem mediaItem) {
        logger.info("Genre matches {} : {}", resultEntry, mediaItem);
        return switch (mediaItem.mediaType()) {
            case MediaType.MOVIE -> resultEntry.category().category().isMovie();
            case MediaType.TV_EPISODE,
                 MediaType.TV_SEASON -> resultEntry.category().category().isTv();
        };
    }
}
