package adudecalledleo.tbsquared.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public final class DefaultMutableDataTracker extends AbstractDataTracker implements MutableDataTracker {
    private DataTracker view;

    private final class ViewDelegate implements DataTracker {
        @Override
        public int size() {
            return DefaultMutableDataTracker.this.size();
        }

        @Override
        public <T> Optional<T> get(DataKey<T> key) {
            return DefaultMutableDataTracker.this.get(key);
        }

        @Override
        public boolean containsKey(DataKey<?> key) {
            return DefaultMutableDataTracker.this.containsKey(key);
        }

        @Override
        public boolean isEmpty() {
            return DefaultMutableDataTracker.this.isEmpty();
        }

        @NotNull
        @Override
        public Iterator<Entry<?>> iterator() {
            return DefaultMutableDataTracker.this.iterator();
        }
    }

    public DefaultMutableDataTracker() {
        super(new HashMap<>());
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
