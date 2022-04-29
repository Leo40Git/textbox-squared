package adudecalledleo.tbsquared.app.plugin.api.config;

public interface ConfigEntryType<T> {
    static ConfigEntryType<Boolean> bool() {
        return BooleanEntryType.INSTANCE;
    }

    static ConfigEntryType<Long> integer() {
        return IntegerEntryType.INSTANCE;
    }

    static ConfigEntryType<Double> floating() {
        return FloatingEntryType.INSTANCE;
    }

    static ConfigEntryType<String> string() {
        return StringEntryType.INSTANCE;
    }

    Class<T> valueClass();
    T defaultValue();
}
