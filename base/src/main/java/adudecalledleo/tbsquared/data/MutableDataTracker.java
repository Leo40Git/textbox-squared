package adudecalledleo.tbsquared.data;

public interface MutableDataTracker extends DataTracker {
    @SuppressWarnings("unchecked")
    static MutableDataTracker copyOf(DataTracker tracker) {
        var copy = new DefaultMutableDataTracker();
        for (var entry : tracker) {
            copy.set((DataKey<Object>) entry.key(), entry.value());
        }
        return copy;
    }

    <T> MutableDataTracker set(DataKey<T> key, T value);
    MutableDataTracker remove(DataKey<?> key);
    void clear();

    /**
     * Returns an immutable view of this data tracker.
     *
     * @return immutable view of this tracker
     */
    DataTracker view();
}
