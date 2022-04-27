package adudecalledleo.tbsquared.app.plugin.config;

import java.util.Optional;
import java.util.regex.Pattern;

@FunctionalInterface
public interface ConfigEntryConstraint<T> {
    static ConfigEntryConstraint<Long> range(long min, long max) {
        return new IntegerRangeConstraint(min, max);
    }

    static ConfigEntryConstraint<Double> range(double min, double max) {
        return new FloatingRangeConstraint(min, max);
    }

    static ConfigEntryConstraint<String> matching(Pattern pattern) {
        return new StringMatchingConstraint(pattern);
    }

    /**
     * @return an error message, or an empty optional if the value is valid.
     */
    Optional<String> testValue(T value);
}
