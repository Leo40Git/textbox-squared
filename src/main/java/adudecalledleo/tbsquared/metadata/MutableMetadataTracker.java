package adudecalledleo.tbsquared.metadata;

public interface MutableMetadataTracker extends MetadataTracker {
    <T> void set(MetadataKey<T> key, T value);
}
