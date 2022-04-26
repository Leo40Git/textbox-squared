package adudecalledleo.tbsquared.app.plugin.config;

public final class BooleanEntryType implements ConfigEntryType<Boolean> {
    public static final BooleanEntryType DEFAULT_TRUE = new BooleanEntryType(true);
    public static final BooleanEntryType DEFAULT_FALSE = new BooleanEntryType(false);

    private final boolean defaultValue;

    private BooleanEntryType(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }
}
