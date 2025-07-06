package gazpacho.core.identify.imdb;

import gazpacho.core.identify.ExternalTitleTokens;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImdbUrlParserTest {

    @Test
    void parse_parsesTitleId() {
        validateMatch("tt11280740", "tt11280740", null);
    }

    @Test
    void parse_parsesTitleUrl() {
        validateMatch("https://www.imdb.com/title/tt0078788/", "tt0078788", null);
        validateMatch("https://www.imdb.com/title/tt0078788", "tt0078788", null);
    }

    @Test
    void parse_parsesTitleUrlWithQueryParameters() {
        validateMatch("https://www.imdb.com/title/tt0078788/?ref_=nv_sr_srsg_0_tt_7_nm_1_in_0_q_apo",
                "tt0078788", null);
    }

    @Test
    void parse_parsesTitleUrlSeason() {
        validateMatch("https://www.imdb.com/title/tt11280740/episodes/?season=2&ref_=ttep",
                "tt11280740", 2);
        validateMatch("https://www.imdb.com/title/tt11280740/episodes/?season=3",
                "tt11280740", 3);
    }

    @Test
    void parse_parsesTitleUrlWithDefaultSeason1() {
        validateMatch("https://www.imdb.com/title/tt11280740/episodes/?ref_=tt_eps",
                "tt11280740", 1);
        validateMatch("https://www.imdb.com/title/tt11280740/episodes/",
                "tt11280740", 1);
        validateMatch("https://www.imdb.com/title/tt11280740/episodes",
                "tt11280740", 1);
    }

    private void validateMatch(String payload, String titleId, Integer season) {
        ImdbUrlParser parser = new ImdbUrlParser();
        Optional<ExternalTitleTokens> expected = Optional.of(ExternalTitleTokens.builder()
                .titleId(titleId)
                .season(season)
                .build());

        Optional<ExternalTitleTokens> result = parser.parse(payload);
        assertTrue(result.isPresent());
        assertEquals(expected.get(), result.get());
    }
}