package adudecalledleo.tbsquared.data;

public interface MutableDataTracker extends DataTracker {
    <T> MutableDataTracker set(DataKey<T> key, T value);
    MutableDataTracker remove(DataKey<?> key);
    void clear();

    /**
     * Returns an immutable view of this metadata tracker.
     *
     * @return immutable view of this tracker
     */
    DataTracker view();
}
