package adudecalledleo.tbsquared.app.plugin;

import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProvider;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class PluginAPI {
    public static void test() {
        PluginManager pMgr = new DefaultPluginManager();
        for (var provider : pMgr.getExtensions(SceneRendererProvider.class)) {
            provider.getId();
        }
    }
}
