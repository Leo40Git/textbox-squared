package adudecalledleo.tbsquared.data;

public interface MutableDataTracker extends DataTracker {
    <T> void set(DataKey<T> key, T value);
}
