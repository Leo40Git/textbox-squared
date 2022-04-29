package adudecalledleo.tbsquared.app.plugin.api.config;

final class FloatingEntryType implements ConfigEntryType<Double> {
    static final ConfigEntryType<Double> INSTANCE = new FloatingEntryType();

    private FloatingEntryType() { }

    @Override
    public Class<Double> valueClass() {
        return Double.class;
    }

    @Override
    public Double defaultValue() {
        return 0.0;
    }
}
