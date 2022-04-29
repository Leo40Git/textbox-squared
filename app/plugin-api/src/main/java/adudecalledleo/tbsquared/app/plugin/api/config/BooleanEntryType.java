package adudecalledleo.tbsquared.app.plugin.api.config;

final class BooleanEntryType implements ConfigEntryType<Boolean> {
    static final ConfigEntryType<Boolean> INSTANCE = new BooleanEntryType();

    private BooleanEntryType() { }

    @Override
    public Class<Boolean> valueClass() {
        return Boolean.class;
    }

    @Override
    public Boolean defaultValue() {
        return false;
    }
}
