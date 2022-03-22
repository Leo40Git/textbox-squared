package adudecalledleo.tbsquared.data;

import java.util.Iterator;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

final class EmptyDataTracker implements DataTracker {
    static final DataTracker INSTANCE = new EmptyDataTracker();
    private static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private EmptyDataTracker() { }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public <T> Optional<T> get(DataKey<T> key) {
        return Optional.empty();
    }

    @Override
    public boolean containsKey(DataKey<?> key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @NotNull
    @Override
    public Iterator<Entry<?>> iterator() {
        return EMPTY_ITERATOR;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataTracker tracker) {
            return tracker.size() == 0;
        }
        return false;
    }

    private static final class EmptyIterator implements Iterator<Entry<?>> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Entry<?> next() {
            throw new UnsupportedOperationException();
        }
    }
}
