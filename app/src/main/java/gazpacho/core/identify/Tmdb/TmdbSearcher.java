package gazpacho.core.identify.Tmdb;

import gazpacho.core.identify.MediaItemQueryTokens;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.TvSeries;
import lombok.NonNull;

import java.util.List;

/**
 *
 */
public interface TmdbSearcher {
    /**
     *
     * @param queryTokens
     * @return
     */
    List<Movie> searchMovies(@NonNull MediaItemQueryTokens queryTokens);

    /**
     *
     * @param queryTokens
     * @return
     */
    List<TvSeries> searchTvSeries(@NonNull MediaItemQueryTokens queryTokens);
}
