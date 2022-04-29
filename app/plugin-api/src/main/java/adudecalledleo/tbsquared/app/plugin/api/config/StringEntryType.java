package adudecalledleo.tbsquared.app.plugin.api.config;

final class StringEntryType implements ConfigEntryType<String> {
    static final ConfigEntryType<String> INSTANCE = new StringEntryType();

    private StringEntryType() { }

    @Override
    public Class<String> valueClass() {
        return String.class;
    }

    @Override
    public String defaultValue() {
        return "";
    }
}
