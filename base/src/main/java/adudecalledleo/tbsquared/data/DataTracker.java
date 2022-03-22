package adudecalledleo.tbsquared.data;

import java.util.Optional;

public interface DataTracker extends Iterable<DataTracker.Entry<?>> {
    static DataTracker empty() {
        return EmptyDataTracker.INSTANCE;
    }

    record Entry<T>(DataKey<T> key, T value) { }

    int size();
    <T> Optional<T> get(DataKey<T> key);
    boolean containsKey(DataKey<?> key);

    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Compares the specified object with this data tracker for equality.<p>
     *
     * This uses a similar contract to {@link java.util.Collection#equals(Object)}: data trackers are considered equal
     * if they have the same size and the same contents.
     */
    boolean equals(Object o);
}
