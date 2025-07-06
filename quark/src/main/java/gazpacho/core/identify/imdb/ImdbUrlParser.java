package gazpacho.core.identify.imdb;

import gazpacho.core.identify.ExternalTitleParser;
import gazpacho.core.identify.ExternalTitleTokens;
import gazpacho.core.util.MatcherUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class ImdbUrlParser implements ExternalTitleParser {
    // persistenceId
    // tt11280740
    // tt0078788
    // link to movie
    // https://www.imdb.com/title/tt0078788/?ref_=nv_sr_srsg_0_tt_7_nm_1_in_0_q_apo
    // https://www.imdb.com/title/tt0078788/
    // link to series -> additional step for the user to select the season
    // https://www.imdb.com/title/tt11280740/
    // https://www.imdb.com/title/tt11280740/?ref_=nv_sr_srsg_1_tt_2_nm_6_in_0_q_severence
    // link to series season
    // https://www.imdb.com/title/tt11280740/episodes/?season=1&ref_=ttep
    // https://www.imdb.com/title/tt11280740/episodes/?ref_=tt_eps -> by default season 1
    // https://www.imdb.com/title/tt31438355/episodes/-> by default season 1
    // link to episode
    // https://www.imdb.com/title/tt13393872/?ref_=ttep_ep_2
    // https://www.imdb.com/title/tt13393872/
    // (www.imdb.com\/title\/)?((tt)((\d)+))(\/)?(episodes\/)?((\?season=)((\d)+))?
    // (www.imdb.com\/title\/)?((tt)((\d)+))(\/)?(episodes\/)?(((\?season=)((\d)+))?(&ref_=ttep)?|(\?ref_=tt_eps))?

    private static final String TITLE_URL_ROOT_REGEX_GROUP = "(www.imdb.com\\/title\\/)?";
    private static final String TITLE_ID_REGEX_GROUP = "((tt)((\\d)+))";
    private static final String DELIM_REGEX_GROUP = "(\\/)?";
    private static final String EPISODES_REGEX_GROUP = "(episodes(\\/)?)?";
    private static final String SEASON_REGEX_GROUP = "((\\?season=)((\\d)+))?(&ref_=ttep)?";
    private static final String EPISODES_QUERY_PARAM_REGEX_GROUP = "(\\?ref_=tt_eps)";
    private static final String OPEN_GROUP = "(";
    private static final String OR_GROUP_DELIM = "|";
    private static final String CLOSE_OPT_GROUP= ")?";


    private static final String PAYLOAD_REGEX_EXP =
            TITLE_URL_ROOT_REGEX_GROUP + TITLE_ID_REGEX_GROUP +
            DELIM_REGEX_GROUP +
            EPISODES_REGEX_GROUP +
            OPEN_GROUP +
            SEASON_REGEX_GROUP +
            OR_GROUP_DELIM +
            EPISODES_QUERY_PARAM_REGEX_GROUP +
            CLOSE_OPT_GROUP;

    private static final Integer TITLE_ID_GROUP_IDX = 2;
    private static final Integer EPISODES_GROUP_IDX = 7;
    private static final Integer SEASON_GROUP_IDX = 12;

    private static final Pattern PATTERN = Pattern.compile(PAYLOAD_REGEX_EXP, Pattern.CASE_INSENSITIVE);

    private static final Integer DEFAULT_SEASON_NUMBER = 1;

    @Override
    public Optional<ExternalTitleTokens> parse(@NonNull String queryString) {
        var imdbTitleTokesBuilder = ExternalTitleTokens.builder();
        Matcher matcher = PATTERN.matcher(queryString);
        if (matcher.find()) {
            Optional<String> titleId = getMatchedTitle(matcher);
            if (titleId.isPresent()) {
                titleId.map(imdbTitleTokesBuilder::titleId);
                getMatchedSeason(matcher).ifPresentOrElse(imdbTitleTokesBuilder::season, () -> {
                    if (matchesEpisodes(matcher)) {
                        imdbTitleTokesBuilder.season(DEFAULT_SEASON_NUMBER);
                    }
                });
            }
            return Optional.of(imdbTitleTokesBuilder.build());
        }
        return Optional.empty();
    }

    private static boolean matchesEpisodes(Matcher matcher) {
        return MatcherUtils.hasMatch(matcher, EPISODES_GROUP_IDX);
    }

    private static Optional<String> getMatchedTitle(Matcher matcher) {
        return MatcherUtils.getMatchedGroupString(matcher, TITLE_ID_GROUP_IDX);
    }

    private static Optional<Integer> getMatchedSeason(Matcher matcher) {
        return MatcherUtils.getMatchedGroupInteger(matcher, SEASON_GROUP_IDX);
    }
}
