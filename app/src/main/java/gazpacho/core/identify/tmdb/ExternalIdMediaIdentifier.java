package gazpacho.core.identify.tmdb;

import gazpacho.core.identify.MediaIdentifier;
import gazpacho.core.identify.ExternalTitleParser;
import gazpacho.core.identify.ExternalTitleTokens;
import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.model.VisualMedia;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbTvSeries;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.find.FindResults;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.find.FindTvEpisode;
import info.movito.themoviedbapi.model.find.FindTvSeason;
import info.movito.themoviedbapi.tools.TmdbException;
import info.movito.themoviedbapi.tools.model.time.ExternalSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ExternalIdMediaIdentifier implements MediaIdentifier {
    @NonNull private final TmdbApi tmdbApi;
    @NonNull private final ExternalTitleParser externalTitleParser;
    @NonNull private final TmdbMediaSelector mediaSelector;
    @NonNull private final ExternalSource externalSource;
    @NonNull private final Logger logger;

    @Override
    public Optional<VisualMedia> identify(@NonNull String identifier) {
        Optional<ExternalTitleTokens> titleTokens = externalTitleParser.parse(identifier);
        try {
            if (titleTokens.isPresent()) {
                return findByExternalId(titleTokens.get());
            }
        } catch (TmdbException e) {
            logger.error("Exception encountered", e);
        }
        return Optional.empty();
    }

    private Optional<VisualMedia> findByExternalId(ExternalTitleTokens titleTokens) throws TmdbException {
        return selectFindResults(getFindApi().findById(titleTokens.titleId(), externalSource, null),
                titleTokens);
    }

    private Optional<VisualMedia> selectFindResults(FindResults findResults, ExternalTitleTokens titleTokens) throws TmdbException {
        if (!findResults.getMovieResults().isEmpty()) {
            return mediaSelector.selectMovie(findResults
                    .getMovieResults()
                    .stream()
                    .map(findMovie -> (Movie) findMovie)
                    .toList(), MediaItemQueryTokens.builder().name("").build());
        }

        if(!findResults.getTvSeriesResults().isEmpty()) {
            return mediaSelector.selectTvSeries(findResults
                    .getTvSeriesResults()
                    .stream()
                    .map(findTvSeries -> (TvSeries) findTvSeries)
                    .toList(),
                    MediaItemQueryTokens.builder()
                            .name("")
                            .season(titleTokens.season())
                            .build());
        }

        if (!findResults.getTvSeasonResults().isEmpty()) {
            FindTvSeason findTvSeason = findResults.getTvSeasonResults().getFirst();
            return Optional.of(getShow(findTvSeason.getShowId(), findTvSeason.getSeasonNumber(), null));
        }

        if (!findResults.getTvEpisodeResults().isEmpty()) {
            FindTvEpisode findTvEpisode = findResults.getTvEpisodeResults().getFirst();
            return Optional.of(getShow(findTvEpisode.getShowId(),
                    findTvEpisode.getSeasonNumber(),
                    findTvEpisode.getEpisodeNumber()));
        }

        return Optional.empty();
    }

    private VisualMedia getShow(Integer showId, Integer season, Integer episode) throws TmdbException {
        return VisualMedia.fromShow(getTvSeriesApi().getDetails(showId, null),
                season, episode);
    }

    private TmdbFind getFindApi() {
        return tmdbApi.getFind();
    }

    private TmdbTvSeries getTvSeriesApi() {
        return tmdbApi.getTvSeries();
    }
}
