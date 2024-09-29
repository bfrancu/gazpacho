package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaIdentifier;
import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.identify.QueryTokensParser;
import gazpacho.core.model.MediaItem;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TmdbMediaIdentifier implements MediaIdentifier {
    @NonNull private final TmdbSearcher searcher;
    @NonNull private final TmdbMediaSelector mediaSelector;
    @NonNull private final QueryTokensParser queryTokensParser;

    @Override
    public Optional<MediaItem> identify(@NonNull String identifier) {
        return queryTokensParser.parse(identifier)
                .map(queryTokens -> {
                    var matchedShow = identifyShow(queryTokens);
                    if (null != queryTokens.season()) {
                        return matchedShow;
                    }

                    var matchedMovie = identifyMovie(queryTokens);
                    if (null == matchedMovie) {
                        return matchedShow;
                    }
                    if (null == matchedShow) {
                        return matchedMovie;
                    }

                    return mediaSelector.selectClosestMatch(matchedMovie, matchedShow, queryTokens);
                });
    }

    private MediaItem identifyShow(MediaItemQueryTokens queryTokens) {
        return mediaSelector.selectTvSeries(searcher.searchTvSeries(queryTokens), queryTokens).orElse(null);
    }

    private MediaItem identifyMovie(MediaItemQueryTokens queryTokens) {
        return mediaSelector.selectMovie(searcher.searchMovies(queryTokens), queryTokens).orElse(null);
    }
}
