package adudecalledleo.tbsquared.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DefaultDataTracker implements MutableDataTracker {
    private final Map<DataKey<?>, Object> values;
    private DataTracker view;

    private final class ViewDelegate implements DataTracker {
        @Override
        public <T> Optional<T> get(DataKey<T> key) {
            return DefaultDataTracker.this.get(key);
        }
    }

    public DefaultDataTracker() {
        values = new HashMap<>();
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
