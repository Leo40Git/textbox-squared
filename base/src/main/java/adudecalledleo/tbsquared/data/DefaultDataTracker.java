package adudecalledleo.tbsquared.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord")
public final class DefaultDataTracker implements DataTracker {
    public static final class Builder {
        private final Map<DataKey<?>, Object> values;

        private Builder() {
            values = new HashMap<>();
        }

        public <T> Builder set(DataKey<T> key, T value) {
            values.put(key, value);
            return this;
        }

        public DataTracker build() {
            return new DefaultDataTracker(Map.copyOf(values));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static <T> DataTracker of(DataKey<T> key, T value) {
        return new DefaultDataTracker(Map.of(key, value));
    }

    private final Map<DataKey<?>, Object> values;

    private DefaultDataTracker(Map<DataKey<?>, Object> values) {
        this.values = values;
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
        return values.keySet();
    }


}
