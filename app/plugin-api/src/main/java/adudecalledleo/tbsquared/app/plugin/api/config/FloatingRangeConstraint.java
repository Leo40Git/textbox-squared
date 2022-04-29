package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.Optional;

public record FloatingRangeConstraint(double min, double max) implements ConfigEntryConstraint<Double> {
    public static FloatingRangeConstraint of(double min, double max) {
        return new FloatingRangeConstraint(min, max);
    }

    public static FloatingRangeConstraint ofMin(double min) {
        return new FloatingRangeConstraint(min, Double.MAX_VALUE);
    }

    public static FloatingRangeConstraint ofMax(double max) {
        return new FloatingRangeConstraint(-Double.MAX_VALUE, max);
    }

    @Override
    public Optional<String> testValue(Double value) {
        if (value <= min || value >= max) {
            return Optional.of("%g is outside of the expected range [%g, %g]".formatted(value, min, max));
        }
        return Optional.empty();
    }
}
