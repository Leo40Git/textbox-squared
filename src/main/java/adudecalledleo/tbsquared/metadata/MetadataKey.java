package adudecalledleo.tbsquared.metadata;

public record MetadataKey<T>(Class<? extends T> type, String name) { }
