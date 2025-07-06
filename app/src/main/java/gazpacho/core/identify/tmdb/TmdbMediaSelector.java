package gazpacho.core.identify.tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import gazpacho.core.model.VisualMedia;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
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
    Optional<VisualMedia> selectMovie(@NonNull List<Movie> movieList,
                                      @NonNull MediaItemQueryTokens queryTokens);

    /**
     *
     * @param seriesList
     * @param queryTokens
     * @return
     */
    Optional<VisualMedia> selectTvSeries(@NonNull List<TvSeries> seriesList,
                                         @NonNull MediaItemQueryTokens queryTokens);

    /**
     *
     * @param movieItem
     * @param showItem
     * @param queryTokens
     * @return
     */
    VisualMedia selectClosestMatch(@NonNull VisualMedia movieItem,
                                   @NonNull VisualMedia showItem,
                                   @NonNull MediaItemQueryTokens queryTokens);
}
