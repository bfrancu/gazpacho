package gazpacho.core.identify;

import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DelimiterQueryTokensParser implements QueryTokensParser {
    // Name, year, season
    // 24 s01
    // lord of the rings fellowship of the rings
    // matrix, 1999
    // beverly hills cop, 2024
    // bad boys ride
    // call me bae season 1
    // southpark season 24, episode 5
    // south park s24e06
    // name | (optional) year | (optional) season | (optional) episode
    // "Sopranos|S01|e02"
    // "Sopranos|S04|eO5"
    //  Sopranos|2004|s05
    // Lord of the rings, fellowship of the rings`

    // episode regex: (^[eE](pisode)?(\s)*((\d){1,3})$)
    // season regex:    (^[sS](eason)?(\s)*((\d\d)|(0?\d))$)
    // year regex: ((19\d\d)|(20\d\d))
    // (^[a-zA-Z0-9\s,!]+)(\|)?((19\d\d)|(20\d\d))?(\|)?([sS](eason)?(\s)*((\d\d)|(0?\d)))?(|)?([eE](pisode)?(\s)*((\d){1,3}))?
    /**
     * ([a-zA-Z0-9\s,!]*)(\|)?((19\d\d)|(20\d\d))?(\|)
     * (^[a-zA-Z0-9\s,!]+)(\|)?((19\d\d)|(20\d\d))?(\|)?([sS](\d\d))?
     * Matrix
     * Matrix|1999
     * Lord of the rings, Fellowship of the rings|2001
     * Sopranos|s04|e01
     * Breaking bad|S05
     * Insula iubirii|s12|e03
     */
    private static final char TOKEN_SEP = '|';
    private static final String SEP_REGEX_GROUP = String.format("((\\s)*\\%s(\\s)*)?", TOKEN_SEP);
    private static final String NAME_REGEX_GROUP = "(^[a-zA-Z0-9\\s,!&]+)";
    private static final String YEAR_REGEX_GROUP = "((19\\d\\d)|(20\\d\\d))?";
    private static final String SEASON_REGEX_GROUP = "([sS](eason)?(\\s)*((\\d\\d)|(0?\\d)))?";
    private static final String EPISODE_REGEX_GROUP = "([eE](pisode)?(\\s)*((\\d){1,3}))?";
    private static final String PAYLOAD_REGEX_EXP =
            NAME_REGEX_GROUP + SEP_REGEX_GROUP +
            YEAR_REGEX_GROUP + SEP_REGEX_GROUP +
            SEASON_REGEX_GROUP + SEP_REGEX_GROUP +
            EPISODE_REGEX_GROUP;

    private static final Integer NAME_GROUP_IDX = 1;
    private static final Integer YEAR_GROUP_IDX = 5;
    private static final Integer SEASON_GROUP_IDX = 14;
    private static final Integer EPISODE_GROUP_IDX = 23;

    private static final Pattern PATTERN = Pattern.compile(PAYLOAD_REGEX_EXP, Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<MediaItemQueryTokens> parse(@NonNull String requestPayload) {
        var queryTokensBuilder = MediaItemQueryTokens.builder();
        Matcher matcher = PATTERN.matcher(requestPayload);
        if (matcher.find()) {
            Optional<String> mediaItemName = getMatchedName(matcher);
            if (mediaItemName.isPresent()) {
                mediaItemName.map(queryTokensBuilder::name);
                getMatchedYear(matcher).ifPresent(queryTokensBuilder::releaseYear);
                getMatchedSeason(matcher).ifPresent(queryTokensBuilder::season);
                getMatchedEpisode(matcher).ifPresent(queryTokensBuilder::episode);
            }
            return Optional.of(queryTokensBuilder.build());
        }
        return Optional.empty();
    }

    private static Optional<String> getMatchedGroupString(Matcher matcher, int index) {
        if (matcher.groupCount() > index && null != matcher.group(index)) {
            return Optional.of(matcher.group(index).strip());
        }
        return Optional.empty();
    }

    private static Optional<Integer> getMatchedGroupInteger(Matcher matcher, int index) {
        return getMatchedGroupString(matcher, index).map(Integer::parseInt);
    }

    private static Optional<String> getMatchedName(Matcher matcher) {
        return getMatchedGroupString(matcher, NAME_GROUP_IDX);
    }

    private static Optional<Integer> getMatchedYear(Matcher matcher) {
        return getMatchedGroupInteger(matcher, YEAR_GROUP_IDX);
    }

    private static Optional<Integer> getMatchedSeason(Matcher matcher) {
        return getMatchedGroupInteger(matcher, SEASON_GROUP_IDX);
    }

    private static Optional<Integer> getMatchedEpisode(Matcher matcher) {
        return getMatchedGroupInteger(matcher, EPISODE_GROUP_IDX);
    }
}
