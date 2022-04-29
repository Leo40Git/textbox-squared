package adudecalledleo.tbsquared.app.plugin.impl;

import adudecalledleo.tbsquared.app.plugin.api.PluginAPI;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PluginAPIHolder {
    private PluginAPIHolder() { }

    private static PluginAPI instance;

    public static PluginAPI getInstance() {
        return instance;
    }

    public static void setInstance(PluginAPI instance) {
        PluginAPIHolder.instance = instance;
    }
}
