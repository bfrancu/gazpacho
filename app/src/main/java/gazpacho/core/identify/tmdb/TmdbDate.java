package gazpacho.core.identify.tmdb;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.Validate;

/**
 *
 */
@Getter
@Accessors(fluent = true)
public class TmdbDate {
    private static final String DELIMITER = "-";
    private static final int YEAR_IDX = 0;
    private static final int MONTH_IDX = 1;
    private static final int DAY_IDX = 2;
    private static final int TOKENS_COUNT = 3;

    private final int year;
    private final int month;
    private final int day;

    /**
     *
     * @param date
     */
    public TmdbDate(@NonNull String date) {
        String[] tokens = date.split(DELIMITER);
        Validate.isTrue((TOKENS_COUNT == tokens.length));
        try {
            this.year = Integer.parseInt(tokens[YEAR_IDX]);
            this.month = Integer.parseInt(tokens[MONTH_IDX]);
            this.day = Integer.parseInt(tokens[DAY_IDX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}
