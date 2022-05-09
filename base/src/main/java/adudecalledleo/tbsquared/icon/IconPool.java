package adudecalledleo.tbsquared.icon;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataKey;

public interface IconPool {
    DataKey<IconPool> ICONS = new DataKey<>(IconPool.class, "icons");
    DataKey<String> PREFIX_ICON = new DataKey<>(String.class, "prefix_icon");

    static IconPool empty() {
        return EmptyIconPool.INSTANCE;
    }

    int getIconSize();

    Map<String, Icon> getIcons();

    default boolean hasIcon(String name) {
        return getIcons().containsKey(name);
    }

    default Icon getIcon(String name) {
        var icon = getIcons().get(name);
        if (icon == null) {
            throw new IllegalArgumentException("Unknown icon name \"" + name + "\"");
        }
        return icon;
    }
}
