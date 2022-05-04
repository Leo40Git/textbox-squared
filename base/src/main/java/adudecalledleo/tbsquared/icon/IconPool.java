package adudecalledleo.tbsquared.icon;

import java.util.LinkedHashMap;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataKey;

public final class IconPool {
    public static final DataKey<IconPool> ICONS = new DataKey<>(IconPool.class, "icons");
    public static final DataKey<String> PREFIX_ICON = new DataKey<>(String.class, "prefix_icon");

    public static final class Builder {
        private final int iconSize;
        private final Map<String, Icon> icons;

        private Builder(int iconSize) {
            this.iconSize = iconSize;
            this.icons = new LinkedHashMap<>();
        }

        public Builder addIcon(String name, Icon icon) {
            if (icon.image().getWidth() != iconSize || icon.image().getHeight() != iconSize) {
                throw new IllegalArgumentException("Icon has invalid size! Should be %d x %d, but is %d x %d!"
                        .formatted(iconSize, iconSize, icon.image().getWidth(), icon.image().getHeight()));
            }
            this.icons.put(name, icon);
            return this;
        }

        public IconPool build() {
            return new IconPool(iconSize, Map.copyOf(icons));
        }
    }

    public static Builder builder(int iconSize) {
        return new Builder(iconSize);
    }

    private static final IconPool EMPTY = new IconPool(0, Map.of());

    public static IconPool empty() {
        return EMPTY;
    }

    private final int iconSize;
    private final Map<String, Icon> icons;

    private IconPool(int iconSize, Map<String, Icon> icons) {
        this.iconSize = iconSize;
        this.icons = icons;
    }

    public int getIconSize() {
        return iconSize;
    }

    public boolean hasIcon(String name) {
        return icons.containsKey(name);
    }

    public Icon getIcon(String name) {
        var icon = icons.get(name);
        if (icon == null) {
            throw new IllegalArgumentException("Unknown icon name \"" + name + "\"");
        }
        return icon;
    }
}
