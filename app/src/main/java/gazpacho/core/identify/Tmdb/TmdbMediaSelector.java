package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.model.MediaItem;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface TmdbMediaSelector {
    /**
     *
     * @param movieList
     * @param queryTokens
     * @return
     */
    Optional<MediaItem> selectMovie(@NonNull List<Movie> movieList,
                                  @NonNull MediaItemQueryTokens queryTokens);

    /**
     *
     * @param seriesList
     * @param queryTokens
     * @return
     */
    Optional<MediaItem> selectTvSeries(@NonNull List<TvSeries> seriesList,
                                        @NonNull MediaItemQueryTokens queryTokens);

    /**
     *
     * @param movieItem
     * @param showItem
     * @param queryTokens
     * @return
     */
    MediaItem selectClosestMatch(@NonNull MediaItem movieItem,
                                 @NonNull MediaItem showItem,
                                 @NonNull MediaItemQueryTokens queryTokens);
}
