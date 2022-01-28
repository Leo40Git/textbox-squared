package adudecalledleo.tbsquared.metadata;

import java.util.Optional;

@FunctionalInterface
public interface MetadataTracker {
    static MetadataTracker empty() {
        return EmptyMetadataTracker.INSTANCE;
    }

    <T> Optional<T> get(MetadataKey<T> key);
}
