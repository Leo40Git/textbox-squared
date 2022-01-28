package adudecalledleo.tbsquared.data;

public record DataKey<T>(Class<? extends T> type, String name) { }
