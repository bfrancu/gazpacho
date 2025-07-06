package gazpacho.core.persistence.service;

import gazpacho.core.model.MediaId;
import gazpacho.core.model.MediaMetadata;
import gazpacho.core.persistence.model.MediaItem;
import gazpacho.core.persistence.model.MediaItemId;
import gazpacho.core.persistence.model.mapper.MediaItemModelMapper;
import gazpacho.core.persistence.repository.MediaItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MediaItemService {
    @Inject
    MediaItemRepository mediaItemRepository;

    @Inject
    MediaItemModelMapper mediaItemModelMapper;

    @Transactional
    public MediaMetadata persist(MediaMetadata metadata) {
        MediaItemId mediaItemId = MediaItemId.fromMediaId(metadata.mediaId());

        if (!mediaItemRepository.existsById(mediaItemId)) {
            return fromPersistence(mediaItemRepository.save(toPersistence(metadata)));
        }

        return metadata;
    }

    private MediaItem toPersistence(MediaMetadata metadata) {
        return mediaItemModelMapper.toPersistenceModel(metadata);
    }

    private MediaMetadata fromPersistence(MediaItem mediaItem) {
        return mediaItemModelMapper.fromPersistenceModel(mediaItem);
    }
}