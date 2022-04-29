package adudecalledleo.tbsquared.app.plugin.api.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public final class ConfigSpec {
    public static final String DEFAULT_SECTION = "general";

    public static final ConfigSpec EMPTY = new ConfigSpec(Map.of());

    private final Map<String, List<ConfigEntry<?>>> sections;

    public static final class Builder {
        private final Map<String, List<ConfigEntry<?>>> sections;
        private String currentSection;

        private Builder() {
            this.sections = new HashMap<>();
            this.currentSection = DEFAULT_SECTION;
        }

        public Builder section(String section) {
            this.currentSection = section;
            return this;
        }

        public Builder add(ConfigEntry<?> entry) {
            this.sections.computeIfAbsent(currentSection, s -> new LinkedList<>()).add(entry);
            return this;
        }

        public ConfigSpec build() {
            return new ConfigSpec(Map.copyOf(sections));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private ConfigSpec(Map<String, List<ConfigEntry<?>>> sections) {
        this.sections = sections;
    }

    public Map<String, List<ConfigEntry<?>>> getSections() {
        return sections;
    }
}
