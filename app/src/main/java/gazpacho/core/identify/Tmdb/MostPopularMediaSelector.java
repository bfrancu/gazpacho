package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.model.VisualMedia;
import gazpacho.core.util.CollectionUtils;
import gazpacho.core.util.KeyExtractor;
import gazpacho.core.util.StringUtils;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.TmdbTvSeries;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import info.movito.themoviedbapi.tools.TmdbException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class MostPopularMediaSelector implements TmdbMediaSelector {
    private static final double MIN_THRESHOLD_TITLE_SIMILARITY = 0.4;

    @NonNull private final TmdbApi tmdbApi;
    @NonNull private final Logger logger;

    @Override
    public Optional<VisualMedia> selectMovie(@NonNull List<Movie> movieList,
                                             @NonNull MediaItemQueryTokens ignored) {
        logger.info("Select movie {}", movieList);
        return getMostPopularMovie(movieList)
                .map(movie -> {
                    try {
                        return VisualMedia.fromMovie(getMoviesApi().getDetails(movie.getId(), movie.getOriginalLanguage()));
                    } catch (TmdbException e) {
                        logger.error("TmdbException caught", e);
                    }
                    return null;
                });
    }

    @Override
    public Optional<VisualMedia> selectTvSeries(@NonNull List<TvSeries> seriesList,
                                                @NonNull MediaItemQueryTokens queryTokens) {
        logger.info("Select show {}, {}", seriesList, queryTokens);
        return getMostPopularShow(seriesList)
                .map(show -> {
                    try {
                        if (checkTvSeriesSeasonsCount(show, queryTokens) &&
                                checkTvSeriesEpisodesCount(show, queryTokens)) {
                            return VisualMedia.fromShow(
                                    getTvSeriesApi().getDetails(show.getId(), show.getOriginalLanguage()),
                                    queryTokens.season(),
                                    queryTokens.episode());
                        }
                    } catch (TmdbException e) {
                        logger.error("TmdbException caught", e);
                    }
                    return null;
                });
    }

    @Override
    public VisualMedia selectClosestMatch(@NonNull VisualMedia firstMatch,
                                          @NonNull VisualMedia secondMatch,
                                          @NonNull MediaItemQueryTokens queryTokens) {
        logger.info("Select closest match first {} second {}", firstMatch, secondMatch);
        var optTvShowMatch = checkForTvShowInQueryTokens(firstMatch, secondMatch, queryTokens);
        if (optTvShowMatch.isPresent()) {
            return optTvShowMatch.get();
        }

        if (fullMatch(firstMatch, queryTokens)) {
            return firstMatch;
        } else if (fullMatch(secondMatch, queryTokens)) {
           return secondMatch;
        }

        double movieTitleSimilarity = StringUtils.similarity(firstMatch.metadata().title(), queryTokens.name());
        double showTitleSimilarity = StringUtils.similarity(secondMatch.metadata().title(), queryTokens.name());

        if (movieTitleSimilarity < MIN_THRESHOLD_TITLE_SIMILARITY &&
                showTitleSimilarity >= MIN_THRESHOLD_TITLE_SIMILARITY) {
            return secondMatch;
        } else if (showTitleSimilarity < MIN_THRESHOLD_TITLE_SIMILARITY &&
                movieTitleSimilarity >= MIN_THRESHOLD_TITLE_SIMILARITY) {
            return firstMatch;
        }

        if (firstMatch.metadata().popularity() > secondMatch.metadata().popularity()) {
            return firstMatch;
        }
        return secondMatch;
    }

    private Optional<VisualMedia> checkForTvShowInQueryTokens(VisualMedia firstMatch,
                                                              VisualMedia secondMatch,
                                                              MediaItemQueryTokens queryTokens) {
        if (null != queryTokens.season()) {
            if (firstMatch.isMovie() && !secondMatch.isMovie()) {
                return Optional.of(secondMatch);
            }

            if (secondMatch.isMovie() && !firstMatch.isMovie()) {
                return Optional.of(firstMatch);
            }
        }
        return Optional.empty();
    }

    private boolean fullMatch(VisualMedia visualMedia, MediaItemQueryTokens queryTokens) {
        boolean titleMatch = visualMedia.metadata()
                .title()
                .toLowerCase(Locale.ROOT)
                .equals(queryTokens.name().toLowerCase(Locale.ROOT));
        return titleMatch && yearMatch(visualMedia, queryTokens);
    }

    private boolean yearMatch(VisualMedia visualMedia, MediaItemQueryTokens queryTokens) {
        if (null != queryTokens.releaseYear()) {
            try {
                return visualMedia.metadata()
                        .firstAirDate().getYear() == queryTokens.releaseYear();
            } catch (IllegalArgumentException e) {
                logger.error("Invalid date format {}", visualMedia.metadata().firstAirDate(), e);
                return false;
            }
        }
        return true;
    }

    private boolean checkTvSeriesEpisodesCount(TvSeries show, MediaItemQueryTokens queryTokens)throws TmdbException {
        if (null != queryTokens.season() && null != queryTokens.episode()) {
            TvSeasonDb seasonDetails = getTvSeasonsApi().getDetails(
                    show.getId(),
                    queryTokens.season(),
                    show.getOriginalLanguage());
            return seasonDetails.getEpisodes().size() >= queryTokens.episode();
        }
        return true;
    }

    private boolean checkTvSeriesSeasonsCount(TvSeries show, MediaItemQueryTokens queryTokens) throws TmdbException {
        if (null != queryTokens.season()) {
            TvSeriesDb showDetails = getTvSeriesApi().getDetails(show.getId(), show.getOriginalLanguage());
            return showDetails.getNumberOfSeasons() >= queryTokens.season();
        }
        return true;
    }

    private static Optional<Movie> getMostPopularMovie(List<Movie> movies) {
        return CollectionUtils.getFirstInSortedList(movies, getPopularityComparator(movie -> movie.getPopularity()));
    }

    private static Optional<TvSeries> getMostPopularShow(List<TvSeries> tvSeries) {
        return CollectionUtils.getFirstInSortedList(tvSeries, getPopularityComparator(show -> show.getPopularity()));
    }

    private static <T> Comparator<T> getPopularityComparator(KeyExtractor<T, Double> keyExtractor) {
        return Collections.reverseOrder(Comparator.comparingDouble(
                item -> null != keyExtractor.extract(item) ? keyExtractor.extract(item) : 0.0));
    }

    private TmdbMovies getMoviesApi() {
        return tmdbApi.getMovies();
    }

    private TmdbTvSeasons getTvSeasonsApi() {
        return tmdbApi.getTvSeasons();
    }

    private TmdbTvSeries getTvSeriesApi() {
        return tmdbApi.getTvSeries();
    }
}
