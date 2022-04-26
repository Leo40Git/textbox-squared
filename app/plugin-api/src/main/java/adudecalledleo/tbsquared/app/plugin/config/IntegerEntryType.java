package adudecalledleo.tbsquared.app.plugin.config;

public final class IntegerEntryType implements ConfigEntryType<Long> {
    private final long defaultValue;
    private final long minimumValue, maximumValue;

    public IntegerEntryType(long defaultValue, long minimumValue, long maximumValue) {
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    public Class<Long> getValueClass() {
        return Long.class;
    }

    @Override
    public Long getDefaultValue() {
        return defaultValue;
    }

    public long getMinimumValue() {
        return minimumValue;
    }

    public long getMaximumValue() {
        return maximumValue;
    }
}
