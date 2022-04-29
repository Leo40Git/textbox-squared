package adudecalledleo.tbsquared.app.plugin.api;

import org.pf4j.PluginManager;

@FunctionalInterface
public interface PostLoadListener {
    void onPostLoad(PluginManager pluginManager);
}
