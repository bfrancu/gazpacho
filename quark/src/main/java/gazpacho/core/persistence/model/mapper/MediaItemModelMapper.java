package gazpacho.core.persistence.model.mapper;

import gazpacho.core.model.MediaId;
import gazpacho.core.model.MediaMetadata;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.service.EntityFetcherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MediaItemModelMapper implements PersistenceModelMapper<MediaMetadata, MediaItem> {
    @Inject
    EntityFetcherService entityFetcherService;

    @Override
    public MediaItem toPersistenceModel(MediaMetadata metadata) {
        MediaItem.MediaItemBuilder builder = MediaItem.builder();

        if (null != metadata.mediaId()) {
            builder = entityFetcherService.fetchMediaItem(metadata.mediaId())
                    .toBuilder();
        }

        builder = builder.id(MediaItemId.fromMediaId(metadata.mediaId()))
                .title(metadata.title())
                .description(metadata.description())
                .language(metadata.language())
                .firstAirDate(metadata.firstAirDate());

        if (null != metadata.seasonsCount()) {
            builder.seasonCount(metadata.seasonsCount());
        }

        return builder.build();
    }

    @Override
    public MediaMetadata fromPersistenceModel(MediaItem mediaItem) {
        return MediaMetadata.builder()
                .mediaId(MediaId.builder()
                        .tmdbId(mediaItem.getId().tmdbId())
                        .mediaType(mediaItem.getId().mediaType())
                        .build())
                .title(mediaItem.getTitle())
                .description(mediaItem.getDescription())
                .language(mediaItem.getLanguage())
                .seasonsCount(mediaItem.getSeasonCount())
                .firstAirDate(mediaItem.getFirstAirDate())
                .build();
    }
}
