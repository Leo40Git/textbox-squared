package adudecalledleo.tbsquared.icon;

import java.util.Map;

final class EmptyIconPool implements IconPool {
    public static final IconPool INSTANCE = new EmptyIconPool();

    private EmptyIconPool() { }

    @Override
    public int getIconSize() {
        return 0;
    }

    @Override
    public Map<String, Icon> getIcons() {
        return Map.of();
    }
}
