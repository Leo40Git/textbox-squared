package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.Optional;

@FunctionalInterface
public interface ConfigEntryConstraint<T> {
    /**
     * @return an error message, or an empty optional if the value is valid.
     */
    Optional<String> testValue(T value);
}
