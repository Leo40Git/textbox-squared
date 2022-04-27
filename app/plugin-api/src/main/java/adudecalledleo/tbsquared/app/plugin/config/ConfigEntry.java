package adudecalledleo.tbsquared.app.plugin.config;

import java.util.*;

import adudecalledleo.tbsquared.data.DataKey;

@SuppressWarnings("ClassCanBeRecord")
public final class ConfigEntry<T> {
    public static final String DEFAULT_DESCRIPTION = "";

    public static final class Builder<T> {
        private final ConfigEntryType<T> type;
        private final DataKey<T> key;

        private String name, description;
        private T defaultValue;
        private final List<ConfigEntryConstraint<T>> constraints;

        private Builder(ConfigEntryType<T> type, DataKey<T> key) {
            this.type = type;
            this.key = key;

            this.name = key.name();
            this.description = DEFAULT_DESCRIPTION;
            this.defaultValue = type.defaultValue();
            this.constraints = new LinkedList<>();
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> constraint(ConfigEntryConstraint<T> constraint) {
            this.constraints.add(constraint);
            return this;
        }

        public Builder<T> constraints(Collection<? extends ConfigEntryConstraint<T>> constraints) {
            this.constraints.addAll(constraints);
            return this;
        }

        @SafeVarargs
        public final Builder<T> constraints(ConfigEntryConstraint<T>... constraints) {
            Collections.addAll(this.constraints, constraints);
            return this;
        }

        public ConfigEntry<T> build() {
            if (!constraints.isEmpty()) {
                List<String> errors = new LinkedList<>();
                for (var constraint : constraints) {
                    constraint.testValue(defaultValue).ifPresent(errors::add);
                }
                if (!errors.isEmpty()) {
                    throw new IllegalArgumentException("Tried to build entry with invalid default value!\n"
                            + "The default value " + defaultValue + " is invalid:\n - "
                            + String.join("\n - ", errors));
                }
            }

            return new ConfigEntry<>(type, key, name, description,
                    defaultValue, List.copyOf(constraints));
        }
    }

    public static <T> Builder<T> builder(ConfigEntryType<T> type, DataKey<T> key) {
        return new Builder<>(type, key);
    }

    private final ConfigEntryType<T> type;
    private final DataKey<T> key;
    private final String name, description;
    private final T defaultValue;
    private final List<ConfigEntryConstraint<T>> constraints;

    private ConfigEntry(ConfigEntryType<T> type, DataKey<T> key, String name, String description,
                       T defaultValue, List<ConfigEntryConstraint<T>> constraints) {
        this.type = type;
        this.key = key;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.constraints = constraints;
    }

    public ConfigEntryType<T> type() {
        return type;
    }

    public DataKey<T> key() {
        return key;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public List<ConfigEntryConstraint<T>> constraints() {
        return constraints;
    }

    public List<String> testValue(T value) {
        if (constraints.isEmpty()) {
            return List.of();
        }

        List<String> errors = new LinkedList<>();
        for (var constraint : constraints) {
            constraint.testValue(value).ifPresent(errors::add);
        }
        return errors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConfigEntry<?>) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.key, that.key) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.defaultValue, that.defaultValue) &&
                Objects.equals(this.constraints, that.constraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, name, description, defaultValue, constraints);
    }

    @Override
    public String toString() {
        return "ConfigEntry[" +
                "type=" + type + ", " +
                "key=" + key + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "defaultValue=" + defaultValue + ", " +
                "constraints=" + constraints + ']';
    }
}
