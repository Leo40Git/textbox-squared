package adudecalledleo.tbsquared.data;

import java.util.Optional;
import java.util.Set;

public interface DataTracker {
    static DataTracker empty() {
        return EmptyDataTracker.INSTANCE;
    }

    <T> Optional<T> get(DataKey<T> key);
    Set<DataKey<?>> getKeys();
}
