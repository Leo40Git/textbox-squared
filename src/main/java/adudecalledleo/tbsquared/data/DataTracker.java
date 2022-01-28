package adudecalledleo.tbsquared.data;

import java.util.Optional;

@FunctionalInterface
public interface DataTracker {
    static DataTracker empty() {
        return EmptyDataTracker.INSTANCE;
    }

    <T> Optional<T> get(DataKey<T> key);
}
