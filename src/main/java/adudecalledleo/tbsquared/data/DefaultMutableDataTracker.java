package adudecalledleo.tbsquared.data;

import java.util.*;

public final class DefaultMutableDataTracker implements MutableDataTracker {
    private final Map<DataKey<?>, Object> values;
    private final Set<DataKey<?>> keyView;
    private DataTracker view;

    private final class ViewDelegate implements DataTracker {
        @Override
        public <T> Optional<T> get(DataKey<T> key) {
            return DefaultMutableDataTracker.this.get(key);
        }

        @Override
        public Set<DataKey<?>> getKeys() {
            return DefaultMutableDataTracker.this.getKeys();
        }
    }

    public DefaultMutableDataTracker() {
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
    public <T> DefaultMutableDataTracker set(DataKey<T> key, T value) {
        values.put(key, value);
        return this;
    }

    @Override
    public DefaultMutableDataTracker remove(DataKey<?> key) {
        values.remove(key);
        return this;
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public DataTracker view() {
        if (view == null) {
            view = new ViewDelegate();
        }
        return view;
    }
}
