package gazpacho.core.util;

import java.util.List;
import java.util.Optional;

public interface ItemSelector<T> {
    Optional<T> select(List<T> items);
}
