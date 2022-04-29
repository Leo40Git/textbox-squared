package adudecalledleo.tbsquared.app.plugin.api.config;

public record ConfigEntryType<T>(Class<T> valueClass, T defaultValue) {
    public static final ConfigEntryType<Boolean> BOOLEAN = new ConfigEntryType<>(Boolean.class, false);
    public static final ConfigEntryType<Long> INTEGER = new ConfigEntryType<>(Long.class, 0L);
    public static final ConfigEntryType<Double> FLOATING = new ConfigEntryType<>(Double.class, 0.0D);
    public static final ConfigEntryType<String> STRING = new ConfigEntryType<>(String.class, "");
}
