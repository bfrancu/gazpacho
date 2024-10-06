package gazpacho.core.model;

import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import lombok.Builder;
import lombok.NonNull;

/**
 *
 * @param title
 * @param description
 * @param language
 * @param originCountry
 * @param firstAirDate
 * @param mediaType
 * @param popularity
 * @param season
 * @param episode
 */
@Builder(toBuilder = true)
public record MediaItem(
        @NonNull String title,
        @NonNull String description,
        @NonNull String language,
        @NonNull String firstAirDate,
        String originCountry,
        @NonNull MediaType mediaType,
        @NonNull Double popularity,
        Integer season,
        Integer episode)
{

    public boolean isMovie() {
        return MediaType.MOVIE == mediaType;
    }
    /**
     *
     * @param show
     * @param season
     * @param episode
     * @return
     */
    public static MediaItem fromShow(TvSeriesDb show, Integer season, Integer episode) {
        var builder = MediaItem.builder()
                .title(show.getName())
                .description(show.getOverview())
                .language(show.getOriginalLanguage())
                .firstAirDate(show.getFirstAirDate())
                .mediaType(null != episode ? MediaType.TV_EPISODE : MediaType.TV_SEASON)
                .popularity(show.getPopularity())
                .season(season)
                .episode(episode);

        if (null != show.getOriginCountry() && !show.getOriginCountry().isEmpty()) {
            builder = builder.originCountry(show.getOriginCountry().getFirst());
        }
        return builder.build();
    }

    /**
     *
     * @param movie
     * @return
     */
    public static MediaItem fromMovie(MovieDb movie) {
        var builder = MediaItem.builder()
                .title(movie.getTitle())
                .firstAirDate(movie.getReleaseDate())
                .popularity(movie.getPopularity())
                .mediaType(MediaType.MOVIE)
                .description(movie.getOverview())
                .language(movie.getOriginalLanguage());

        if (null != movie.getProductionCountries() && !movie.getProductionCountries().isEmpty()) {
            builder = builder.originCountry(movie.getProductionCountries().getFirst().getName());
        }
        return builder.build();
    }
}
