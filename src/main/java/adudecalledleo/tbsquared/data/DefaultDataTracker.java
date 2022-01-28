package adudecalledleo.tbsquared.data;

import java.util.*;

public final class DefaultDataTracker implements MutableDataTracker {
    private final Map<DataKey<?>, Object> values;
    private final Set<DataKey<?>> keyView;
    private DataTracker view;

    private final class ViewDelegate implements DataTracker {
        @Override
        public <T> Optional<T> get(DataKey<T> key) {
            return DefaultDataTracker.this.get(key);
        }

        @Override
        public Set<DataKey<?>> getKeys() {
            return DefaultDataTracker.this.getKeys();
        }
    }

    public DefaultDataTracker() {
        values = new HashMap<>();
        keyView = Collections.unmodifiableSet(values.keySet());
    }

    @Override
    public <T> Optional<T> get(DataKey<T> key) {
        Object rawValue = values.get(key);
        if (!key.type().isInstance(rawValue)) {
            return Optional.empty();
        }
        return Optional.of(key.type().cast(rawValue));
    }

    @Override
    public Set<DataKey<?>> getKeys() {
        return keyView;
    }

    @Override
    public <T> void set(DataKey<T> key, T value) {
        values.put(key, value);
    }

    /**
     * Gets an immutable view of this metadata tracker.
     *
     * @return immutable view of this tracker
     */
    public DataTracker view() {
        if (view == null) {
            view = new ViewDelegate();
        }
        return view;
    }
}
