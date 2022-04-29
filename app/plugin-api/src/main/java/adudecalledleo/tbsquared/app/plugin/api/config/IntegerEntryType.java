package adudecalledleo.tbsquared.app.plugin.api.config;

final class IntegerEntryType implements ConfigEntryType<Long> {
    static final ConfigEntryType<Long> INSTANCE = new IntegerEntryType();

    private IntegerEntryType() { }

    @Override
    public Class<Long> valueClass() {
        return Long.class;
    }

    @Override
    public Long defaultValue() {
        return 0L;
    }
}
