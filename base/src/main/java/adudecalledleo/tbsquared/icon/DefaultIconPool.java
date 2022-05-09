package adudecalledleo.tbsquared.icon;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DefaultIconPool implements IconPool {
    public static final class Builder {
        private final int iconSize;
        private final Map<String, Icon> icons;

        private Builder(int iconSize) {
            this.iconSize = iconSize;
            this.icons = new LinkedHashMap<>();
        }

        public Builder addIcon(Icon icon) {
            if (icon.image().getWidth() != iconSize || icon.image().getHeight() != iconSize) {
                throw new IllegalArgumentException("Icon has invalid size! Should be %d x %d, but is %d x %d!"
                        .formatted(iconSize, iconSize, icon.image().getWidth(), icon.image().getHeight()));
            }
            this.icons.put(icon.name(), icon);
            return this;
        }

        public IconPool build() {
            return new DefaultIconPool(iconSize, Map.copyOf(icons));
        }
    }

    public static Builder builder(int iconSize) {
        return new Builder(iconSize);
    }

    private final int iconSize;
    private final Map<String, Icon> icons;

    private DefaultIconPool(int iconSize, Map<String, Icon> icons) {
        this.iconSize = iconSize;
        this.icons = icons;
    }

    @Override
    public int getIconSize() {
        return iconSize;
    }

    @Override
    public Map<String, Icon> getIcons() {
        return icons;
    }
}
