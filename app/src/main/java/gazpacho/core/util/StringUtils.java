package gazpacho.core.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.text.similarity.LevenshteinDistance;

@UtilityClass
public class StringUtils {
    public double distance(@NonNull String s1, @NonNull String s2) {
        return LevenshteinDistance.getDefaultInstance().apply(s1, s2);
    }

    public double similarity(@NonNull String s1, @NonNull String s2) {
        String longer = s1;
        String shorter = s2;
        if (longer.length() < shorter.length()) {
            longer = s2;
            shorter = s1;
        }

        if (longer.isEmpty()) {
            return 1.0;
        }

        return truncate((longer.length() - distance(longer, shorter)) / (double) longer.length());
    }

    private double truncate(double d) {
        return Math.floor(d * 100) / 100;
    }
}
