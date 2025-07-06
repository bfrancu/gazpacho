package gazpacho.core.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.regex.Matcher;

@UtilityClass
public class MatcherUtils {
    public Optional<String> getMatchedGroupString(Matcher matcher, int index) {
        if (matcher.groupCount() > index && null != matcher.group(index)) {
            return Optional.of(matcher.group(index).strip());
        }
        return Optional.empty();
    }

    public Optional<Integer> getMatchedGroupInteger(Matcher matcher, int index) {
        return getMatchedGroupString(matcher, index).map(Integer::parseInt);
    }

    public boolean hasMatch(Matcher matcher, int index) {
        return matcher.groupCount() > index && null != matcher.group(index);
    }
}
