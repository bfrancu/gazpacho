package gazpacho.core.util;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;

public class OrderedHierarchyComparator<T> implements Comparator<T> {

    private final ImmutableList<Comparator<T>> orderedComparatorList;

    public OrderedHierarchyComparator(@NonNull List<Comparator<T>> orderedComparatorList) {
        this.orderedComparatorList = ImmutableList.copyOf(orderedComparatorList);
    }

    @Override
    public int compare(T left, T right) {
        for (var comp : orderedComparatorList) {
            int result = comp.compare(left, right);
            if (0 != result) {
                return result;
            }
        }

        return 0;
    }
}
