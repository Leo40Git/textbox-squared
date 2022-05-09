package adudecalledleo.tbsquared.icon;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AggregateIconPool implements IconPool {
    private final int iconSize;
    private final Map<String, Icon> icons;

    public AggregateIconPool(int iconSize, IconPool... pools) {
        this.iconSize = iconSize;
        Map<String, Icon> iconMap = new LinkedHashMap<>();
        for (var pool : pools) {
            if (iconSize != pool.getIconSize()) {
                throw new IllegalArgumentException("Inconsistent icon size");
            }
            iconMap.putAll(pool.getIcons());
        }
        this.icons = Map.copyOf(iconMap);
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
