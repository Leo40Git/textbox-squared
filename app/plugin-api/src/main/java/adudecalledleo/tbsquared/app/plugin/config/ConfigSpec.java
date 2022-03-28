package adudecalledleo.tbsquared.app.plugin.config;

import java.util.*;
import java.util.function.Consumer;

import adudecalledleo.tbsquared.data.DataKey;
import org.jetbrains.annotations.NotNull;

public final class ConfigSpec implements Iterable<ConfigEntry<?>> {
    private final List<ConfigEntry<?>> entries;
    private final Map<String, List<ConfigEntry<?>>> byCategories;

    public static final class Builder {
        private final List<ConfigEntry<?>> entries;

        private Builder() {
            entries = new ArrayList<>();
        }

        public Builder add(ConfigEntry<?> entry) {
            entries.add(entry);
            return this;
        }

        public final class Category {
            private final String category;

            private Category(String category) {
                this.category = category;
            }

            public <T> Category add(ConfigEntryType<T> type, DataKey<T> key, String name, String description) {
                Builder.this.add(new ConfigEntry<>(type, key, category, name, description));
                return this;
            }
        }

        public Builder category(String name, Consumer<Category> action) {
            action.accept(new Category(name));
            return this;
        }

        public ConfigSpec build() {
            return new ConfigSpec(List.copyOf(entries));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private ConfigSpec(List<ConfigEntry<?>> entries) {
        this.entries = entries;

        var byCategoriesMap = new HashMap<String, List<ConfigEntry<?>>>();
        for (var entry : entries) {
            byCategoriesMap.computeIfAbsent(entry.category(), s -> new ArrayList<>()).add(entry);
        }
        for (var entry : byCategoriesMap.entrySet()) {
            entry.setValue(List.copyOf(entry.getValue()));
        }
        byCategories = Map.copyOf(byCategoriesMap);
    }

    @NotNull
    @Override
    public Iterator<ConfigEntry<?>> iterator() {
        return entries.iterator();
    }

    @Override
    public Spliterator<ConfigEntry<?>> spliterator() {
        return entries.spliterator();
    }

    @Override
    public void forEach(Consumer<? super ConfigEntry<?>> action) {
        entries.forEach(action);
    }

    public Map<String, List<ConfigEntry<?>>> byCategories() {
        return byCategories;
    }
}
