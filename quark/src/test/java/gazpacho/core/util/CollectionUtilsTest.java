package gazpacho.core.util;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class CollectionUtilsTest {
    @Test
    public void getFirstInSortedList_returnsFirstElementBySortOrder() {
        var integerList = new ArrayList<>();
        integerList.addAll(List.of(21, 54, 26, 61, 3, 12, 91, 48, 9, 44));
        var optFirstValue = CollectionUtils.getFirstInSortedList(integerList,
                Comparator.comparingInt(i -> (int) i));
        assertEquals(3, optFirstValue.get());
    }
}