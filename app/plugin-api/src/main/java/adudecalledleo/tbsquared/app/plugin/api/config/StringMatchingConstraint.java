package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.Optional;
import java.util.regex.Pattern;

record StringMatchingConstraint(Pattern pattern) implements ConfigEntryConstraint<String> {
    @Override
    public Optional<String> testValue(String value) {
        if (!pattern.matcher(value).matches()) {
            return Optional.of("\"%s\" does not match pattern \"%s\"".formatted(value, pattern));
        }
        return Optional.empty();
    }
}
