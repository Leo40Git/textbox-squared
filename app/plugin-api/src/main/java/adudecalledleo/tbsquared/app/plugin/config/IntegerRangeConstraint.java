package adudecalledleo.tbsquared.app.plugin.config;

import java.util.Optional;

record IntegerRangeConstraint(long min, long max) implements ConfigEntryConstraint<Long> {
    @Override
    public Optional<String> testValue(Long value) {
        if (value <= min || value >= max) {
            return Optional.of("%d is outside of the expected range [%d, %d]".formatted(value, min, max));
        }
        return Optional.empty();
    }
}
