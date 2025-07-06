package gazpacho.core.util;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class CollectionUtils {
    public <T> Optional<T> getFirstInSortedList(List<T> items, Comparator<T> comp) {
        items.removeAll(Collections.singleton(null));
        if (items.isEmpty()) {
            return Optional.empty();
        }
        items.sort(comp);
        return Optional.of(items.getFirst());
    }
}
