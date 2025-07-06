package gazpacho.core.model;

import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.tv.core.TvEpisode;
import info.movito.themoviedbapi.model.tv.series.TvSeriesDb;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

/**
 *
 * @param metadata
 * @param release
 * @param nextRelease
 */
@Builder(toBuilder = true)
public record VisualMedia(
        @NonNull MediaMetadata metadata,
        @NonNull MediaRelease release,
        MediaRelease nextRelease) {

    public boolean isMovie() {
        return MediaReleaseType.MOVIE == release.mediaReleaseType();
    }

    public boolean isSeason() {
        return MediaReleaseType.TV_SEASON == release.mediaReleaseType();
    }

    public boolean isEpisode() {
        return MediaReleaseType.TV_EPISODE == release.mediaReleaseType();
    }

    public static class VisualMediaBuilder {
        private MediaId mediaId;
        private MediaMetadata metadata;
        private MediaRelease release;

        public VisualMediaBuilder mediaId(@NonNull MediaId mediaId) {
            this.mediaId = mediaId;
            return this;
        }

        public VisualMedia build() {
            if (null != mediaId) {
                metadata = metadata.toBuilder()
                        .mediaId(mediaId)
                        .build();
                release = release.toBuilder()
                        .mediaId(mediaId)
                        .build();
            }
            return new VisualMedia(metadata, release, nextRelease);
        }
    }

    /**
     *
     * @param show
     * @param season
     * @param episode
     * @return
     */
    public static VisualMedia fromShow(TvSeriesDb show, Integer season, Integer episode) {
        var builder = VisualMedia.builder()
                .metadata(getMetadata(show))
                .release(MediaRelease.builder()
                        .mediaId(getId(show))
                        .mediaReleaseType(null != episode ? MediaReleaseType.TV_EPISODE : MediaReleaseType.TV_SEASON)
                        .releaseDate(LocalDate.parse(show.getLastAirDate()))
                        .season(season)
                        .episode(episode)
                        .build());

        if (null != show.getNextEpisodeToAir()) {
            TvEpisode nextEpisodeToAir = show.getNextEpisodeToAir();
            builder.nextRelease(MediaRelease.builder()
                    .mediaId(getId(show))
                    .releaseDate(LocalDate.parse(nextEpisodeToAir.getAirDate()))
                    .mediaReleaseType(MediaReleaseType.TV_EPISODE)
                    .season(nextEpisodeToAir.getSeasonNumber())
                    .episode(nextEpisodeToAir.getEpisodeNumber())
                    .build());
        }

        return builder.build();
    }

    /**
     *
     * @param movie
     * @return
     */
    public static VisualMedia fromMovie(MovieDb movie) {
        return VisualMedia.builder()
                .metadata(getMetadata(movie))
                .release(MediaRelease.builder()
                        .mediaId(getId(movie))
                        .releaseDate(LocalDate.parse(movie.getReleaseDate()))
                        .mediaReleaseType(MediaReleaseType.MOVIE)
                        .build())
                .build();
    }

    private static MediaId getId(TvSeriesDb show) {
        return MediaId.builder()
                .tmdbId((long) show.getId())
                .mediaType(MediaType.TV)
                .build();
    }

    private static MediaMetadata getMetadata(TvSeriesDb show) {
        var builder = MediaMetadata.builder()
                .mediaId(getId(show))
                .title(show.getName())
                .description(show.getOverview())
                .language(show.getOriginalLanguage())
                .firstAirDate(LocalDate.parse(show.getFirstAirDate()))
                .popularity(show.getPopularity());

        if (null != show.getOriginCountry() && !show.getOriginCountry().isEmpty()) {
            builder = builder.originCountry(show.getOriginCountry().getFirst());
        }
        return builder.build();
    }

    private static MediaId getId(TvSeries show) {
        return MediaId.builder()
                .tmdbId((long) show.getId())
                .mediaType(MediaType.TV)
                .build();
    }

    private static MediaMetadata getMetadata(TvSeries show) {
        var builder = MediaMetadata.builder()
                .mediaId(getId(show))
                .title(show.getName())
                .description(show.getOverview())
                .language(show.getOriginalLanguage())
                .firstAirDate(LocalDate.parse(show.getFirstAirDate()))
                .popularity(show.getPopularity());

        if (null != show.getOriginCountry() && !show.getOriginCountry().isEmpty()) {
            builder = builder.originCountry(show.getOriginCountry().getFirst());
        }
        return builder.build();
    }

    private static MediaId getId(MovieDb movie) {
        return MediaId.builder()
                .tmdbId((long) movie.getId())
                .mediaType(MediaType.MOVIE)
                .build();
    }

    private static MediaMetadata getMetadata(MovieDb movie) {
        var builder = MediaMetadata.builder()
                .mediaId(getId(movie))
                .title(movie.getTitle())
                .firstAirDate(LocalDate.parse(movie.getReleaseDate()))
                .popularity(movie.getPopularity())
                .description(movie.getOverview())
                .language(movie.getOriginalLanguage());

        if (null != movie.getProductionCountries() && !movie.getProductionCountries().isEmpty()) {
            builder = builder.originCountry(movie.getProductionCountries().getFirst().getName());
        }
        return builder.build();
    }
}
