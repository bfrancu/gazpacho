package gazpacho.core.identify;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DelimiterQueryTokensParserTest {

    @Test
    public void match_matchesThePattern() {
        validateMatch("Sopranos|2004|s02|episode 5", "Sopranos",
                2004, 2, 5);
    }

    @Test
    public void match_matchesSingleTitle() {
        validateMatch("Lord of the rings", "Lord of the rings", null, null, null);
    }

    @Test
    public void match_matchesTitleWithStripedWhitespace() {
        validateMatch("   Dark Knight     | 2003     ", "Dark Knight", 2003, null, null);
    }

    @Test
    public void match_matchesTitleYear() {
        validateMatch("Matrix|1999", "Matrix", 1999, null, null);
    }

    @Test
    public void match_matchesTitleWithSpecialCharacter() {
        validateMatch("Malcolm & Marie", "Malcolm & Marie", null, null, null);
    }

    @Test
    public void match_matchesTitleYearSeason() {
        validateMatch("Severance|2022|s01", "Severance", 2022, 1, null);
    }

    @Test
    public void match_matchesTitleYearSeasonLongVersion() {
        validateMatch("Game of thrones|2011|season8", "Game of thrones", 2011, 8, null);
    }

    @Test
    public void match_matchesTitleYearSeasonLongVersionWithSpaces() {
        validateMatch("Game of thrones|2011|season     7", "Game of thrones", 2011, 7, null);
    }

    @Test
    public void match_matchesTitleYearSeasonLongVersionWithCapitalS() {
        validateMatch("Umbrella Academy|2016|Season 05", "Umbrella Academy", 2016, 5, null);
    }

    @Test
    public void match_matchesTitleSeason() {
        validateMatch("Breaking Bad|S02", "Breaking Bad", null, 2, null);
    }

    @Test
    public void match_matchesTitleSeasonEpisode() {
        validateMatch("Umbre|s01|e07", "Umbre", null, 1, 7);
    }

    @Test
    public void match_doesNotMatchTitleEpisode() {
    }

    private void validateMatch(String payload, String name, Integer releaseYear,
                               Integer season, Integer episode) {
        DelimiterQueryTokensParser parser = new DelimiterQueryTokensParser();
        var optQueryTokens = parser.parse(payload);
        assertTrue(optQueryTokens.isPresent());
        MediaItemQueryTokens queryTokens = optQueryTokens.get();
        validateTitleYearSeason(queryTokens, name, releaseYear, season, episode);
    }

    private static void validateTitleYearSeason(MediaItemQueryTokens queryTokens, String name,
                                                Integer releaseYear, Integer season, Integer episode) {
        assertEquals(name, queryTokens.name());
        if (null != releaseYear) {
            assertEquals(releaseYear, queryTokens.releaseYear());
        } else {
            assertNull(queryTokens.releaseYear());
        }
        if (null != season) {
            assertEquals(season, queryTokens.season());
        } else {
            assertNull(queryTokens.season());
        }
        if (null != episode) {
            assertEquals(episode, queryTokens.episode());
        } else {
            assertNull(queryTokens.episode());
        }
    }
}
