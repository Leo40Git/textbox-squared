package adudecalledleo.tbsquared.data;

import java.util.Map;
import java.util.Optional;

public interface DataTracker extends Iterable<DataTracker.Entry<?>> {
    static DataTracker empty() {
        return EmptyDataTracker.INSTANCE;
    }

    static DataTrackerBuilder builder() {
        return new DataTrackerBuilder();
    }

    static <T1> DataTracker of(DataKey<T1> k1, T1 v1) {
        return new DefaultDataTracker(Map.of(k1, v1));
    }

    static <T1, T2> DataTracker of(DataKey<T1> k1, T1 v1,
                                   DataKey<T2> k2, T2 v2) {
        return new DefaultDataTracker(Map.of(k1, v1, k2, v2));
    }

    static <T1, T2, T3> DataTracker of(DataKey<T1> k1, T1 v1,
                                       DataKey<T2> k2, T2 v2,
                                       DataKey<T3> k3, T3 v3) {
        return new DefaultDataTracker(Map.of(k1, v1, k2, v2, k3, v3));
    }

    static <T1, T2, T3, T4> DataTracker of(DataKey<T1> k1, T1 v1,
                                           DataKey<T2> k2, T2 v2,
                                           DataKey<T3> k3, T3 v3,
                                           DataKey<T4> k4, T4 v4) {
        return new DefaultDataTracker(Map.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    static DataTracker copyOf(DataTracker tracker) {
        if (tracker.size() == 0) {
            return empty();
        } else if (tracker instanceof DefaultDataTracker) {
            return tracker;
        }
        return builder().setAll(tracker).build();
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
