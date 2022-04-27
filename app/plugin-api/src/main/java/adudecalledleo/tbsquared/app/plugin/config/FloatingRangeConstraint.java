package adudecalledleo.tbsquared.app.plugin.config;

import java.util.Optional;

record FloatingRangeConstraint(double min, double max) implements ConfigEntryConstraint<Double> {
    @Override
    public Optional<String> testValue(Double value) {
        if (value <= min || value >= max) {
            return Optional.of("%g is outside of the expected range [%g, %g]".formatted(value, min, max));
        }
        return Optional.empty();
    }
}
