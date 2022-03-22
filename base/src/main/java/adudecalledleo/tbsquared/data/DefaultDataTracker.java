package adudecalledleo.tbsquared.data;

import java.util.HashMap;
import java.util.Map;

public final class DefaultDataTracker extends AbstractDataTracker {
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

    private DefaultDataTracker(Map<DataKey<?>, Object> values) {
        super(values);
    }
}
