package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.Optional;
import java.util.regex.Pattern;

public record StringMatchingConstraint(Pattern pattern) implements ConfigEntryConstraint<String> {
    public static StringMatchingConstraint of(Pattern pattern) {
        return new StringMatchingConstraint(pattern);
    }

    public static StringMatchingConstraint of(String pattern) {
        return new StringMatchingConstraint(Pattern.compile(pattern));
    }

    @Override
    public Optional<String> testValue(String value) {
        if (!pattern.matcher(value).matches()) {
            return Optional.of("\"%s\" does not match pattern \"%s\"".formatted(value, pattern));
        }
        return Optional.empty();
    }
}
