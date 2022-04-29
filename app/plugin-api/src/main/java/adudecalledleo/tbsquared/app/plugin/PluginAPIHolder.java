package adudecalledleo.tbsquared.app.plugin;

final class PluginAPIHolder {
    private PluginAPIHolder() { }

    private static PluginAPI instance;

    public static PluginAPI getInstance() {
        return instance;
    }

    public static void setInstance(PluginAPI instance) {
        PluginAPIHolder.instance = instance;
    }
}
