package adudecalledleo.tbsquared.app.plugin.config;

public final class FloatEntryType implements ConfigEntryType<Double> {
    private final double defaultValue;
    private final double minimumValue, maximumValue;

    public FloatEntryType(double defaultValue, double minimumValue, double maximumValue) {
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    public Class<Double> getValueClass() {
        return Double.class;
    }

    @Override
    public Double getDefaultValue() {
        return defaultValue;
    }

    public double getMinimumValue() {
        return minimumValue;
    }

    public double getMaximumValue() {
        return maximumValue;
    }
}
