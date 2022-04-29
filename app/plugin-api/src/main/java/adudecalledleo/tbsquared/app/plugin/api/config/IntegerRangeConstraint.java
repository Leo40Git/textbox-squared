package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.Optional;

public record IntegerRangeConstraint(long min, long max) implements ConfigEntryConstraint<Long> {
    public static IntegerRangeConstraint of(long min, long max) {
        return new IntegerRangeConstraint(min, max);
    }

    public static IntegerRangeConstraint ofMin(long min) {
        return new IntegerRangeConstraint(min, Long.MAX_VALUE);
    }

    public static IntegerRangeConstraint ofMax(long max) {
        return new IntegerRangeConstraint(Long.MIN_VALUE, max);
    }

    @Override
    public Optional<String> testValue(Long value) {
        if (value <= min || value >= max) {
            return Optional.of("%d is outside of the expected range [%d, %d]".formatted(value, min, max));
        }
        return Optional.empty();
    }
}
