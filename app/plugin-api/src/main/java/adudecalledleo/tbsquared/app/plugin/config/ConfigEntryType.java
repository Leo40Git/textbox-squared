package adudecalledleo.tbsquared.app.plugin.config;

public interface ConfigEntryType<T> {
    Class<T> valueClass();
    T defaultValue();
}
