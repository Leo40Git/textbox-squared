package adudecalledleo.tbsquared.app.plugin.config;

public final class StringEntryType implements ConfigEntryType<String> {
    private final String defaultValue;
    private final boolean multiline;
    private final int maximumLength;

    public StringEntryType(String defaultValue, boolean multiline, int maximumLength) {
        this.defaultValue = defaultValue;
        this.multiline = multiline;
        this.maximumLength = maximumLength;
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public int getMaximumLength() {
        return maximumLength;
    }
}
