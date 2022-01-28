package adudecalledleo.tbsquared.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DefaultMetadataTracker implements MutableMetadataTracker {
    private final Map<MetadataKey<?>, Object> values;
    private MetadataTracker view;

    private final class ViewDelegate implements MetadataTracker {
        @Override
        public <T> Optional<T> get(MetadataKey<T> key) {
            return DefaultMetadataTracker.this.get(key);
        }
    }

    public DefaultMetadataTracker() {
        values = new HashMap<>();
    }

    @Override
    public <T> Optional<T> get(MetadataKey<T> key) {
        Object rawValue = values.get(key);
        if (!key.type().isInstance(rawValue)) {
            return Optional.empty();
        }
        return Optional.of(key.type().cast(rawValue));
    }

    @Override
    public <T> void set(MetadataKey<T> key, T value) {
        values.put(key, value);
    }

    /**
     * Gets an immutable view of this metadata tracker.
     *
     * @return immutable view of this tracker
     */
    public MetadataTracker view() {
        if (view == null) {
            view = new ViewDelegate();
        }
        return view;
    }
}
