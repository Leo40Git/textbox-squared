package adudecalledleo.tbsquared.metadata;

import java.util.Optional;

final class EmptyMetadataTracker implements MetadataTracker {
    static final MetadataTracker INSTANCE = new EmptyMetadataTracker();

    private EmptyMetadataTracker() { }

    @Override
    public <T> Optional<T> get(MetadataKey<T> key) {
        return Optional.empty();
    }
}
