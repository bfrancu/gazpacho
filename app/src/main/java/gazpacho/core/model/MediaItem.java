package gazpacho.core.model;

import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

/**
 *
 * @param title
 * @param description
 * @param language
 * @param originCountry
 * @param firstAirDate
 * @param mediaReleaseType
 * @param popularity
 * @param season
 * @param episode
 */
@Builder(toBuilder = true)
public record MediaItem(
        @NonNull String title,
        @NonNull String description,
        @NonNull String language,
        @NonNull LocalDate firstAirDate,
        String originCountry,
        @NonNull MediaReleaseType mediaReleaseType,
        @NonNull Double popularity,
        Integer season,
        Integer episode)
{

    public boolean isMovie() {
        return MediaReleaseType.MOVIE == mediaReleaseType;
    }

    public boolean isSeason() {
        return MediaReleaseType.TV_SEASON == mediaReleaseType;
    }

    public boolean isEpisode() {
        return MediaReleaseType.TV_EPISODE == mediaReleaseType;
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
                .firstAirDate(LocalDate.parse(show.getFirstAirDate()))
                .mediaReleaseType(null != episode ? MediaReleaseType.TV_EPISODE : MediaReleaseType.TV_SEASON)
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
                .firstAirDate(LocalDate.parse(movie.getReleaseDate()))
                .popularity(movie.getPopularity())
                .mediaReleaseType(MediaReleaseType.MOVIE)
                .description(movie.getOverview())
                .language(movie.getOriginalLanguage());

        if (null != movie.getProductionCountries() && !movie.getProductionCountries().isEmpty()) {
            builder = builder.originCountry(movie.getProductionCountries().getFirst().getName());
        }
        return builder.build();
    }
}
