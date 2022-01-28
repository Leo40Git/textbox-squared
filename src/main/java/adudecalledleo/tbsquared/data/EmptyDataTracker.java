package adudecalledleo.tbsquared.data;

import java.util.Optional;
import java.util.Set;

final class EmptyDataTracker implements DataTracker {
    static final DataTracker INSTANCE = new EmptyDataTracker();

    private EmptyDataTracker() { }

    @Override
    public <T> Optional<T> get(DataKey<T> key) {
        return Optional.empty();
    }

    @Override
    public Set<DataKey<?>> getKeys() {
        return Set.of();
    }
}
