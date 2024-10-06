package gazpacho.core.identify.Tmdb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TmdbDateTest {

    @Test
    public void constructor_throwsIllegalArgumentException_whenWrongTokensCount() {
        String date = "1999-01-02-03";
        assertThrows(IllegalArgumentException.class, () -> new TmdbDate(date));
    }

    @Test
    public void constructor_throwsIllegalArgumentException_whenInvalidFormat() {
        String invalidDate = "2005-04-second";
        assertThrows(IllegalArgumentException.class, () -> new TmdbDate(invalidDate));
    }

    @Test
    public void constructor_createsTmdbDate_whenValidStringFormat() {
        int year = 2011;
        int month = 10;
        int day = 15;
        String date = String.format("%s-%s-%s", year, month, day);

        var tmdbDate = new TmdbDate(date);
        assertEquals(year, tmdbDate.year());
        assertEquals(month, tmdbDate.month());
        assertEquals(day, tmdbDate.day());
    }
}
