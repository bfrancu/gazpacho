package gazpacho.core.identify;

import com.google.common.collect.ImmutableList;
import gazpacho.core.model.VisualMedia;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CompositeMediaIdentifier implements MediaIdentifier {
    @NonNull private final ImmutableList<MediaIdentifier> mediaIdentifiers;

    @Override
    public Optional<VisualMedia> identify(@NonNull String payload) {
        for (MediaIdentifier identifier : mediaIdentifiers) {
            Optional<VisualMedia> result = identifier.identify(payload);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }
}
