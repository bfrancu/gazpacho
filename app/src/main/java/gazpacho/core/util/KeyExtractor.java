package gazpacho.core.util;

@FunctionalInterface
public interface KeyExtractor<T, K> {
    K extract(T value);
}
