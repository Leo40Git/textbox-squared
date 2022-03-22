package adudecalledleo.tbsquared.data;

import java.util.HashMap;
import java.util.Map;

public final class DataTrackerBuilder {
    private final Map<DataKey<?>, Object> values;

    public DataTrackerBuilder() {
        values = new HashMap<>();
    }

    public <T> DataTrackerBuilder set(DataKey<T> key, T value) {
        values.put(key, value);
        return this;
    }

    public DataTrackerBuilder setAll(DataTracker tracker) {
        for (var entry : tracker) {
            values.put(entry.key(), entry.value());
        }
        return this;
    }

    public DataTracker build() {
        return new DefaultDataTracker(Map.copyOf(values));
    }
}
