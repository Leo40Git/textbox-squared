package adudecalledleo.tbsquared.app;

import java.nio.file.Paths;

import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.impl.PluginAPIImpl;
import adudecalledleo.tbsquared.app.plugin.impl.renderer.SceneRendererProviderRegistry;
import org.pf4j.DefaultPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bootstrap {
    private Bootstrap() { }

    public static final Logger LOGGER;

    static {
        System.setProperty("log4j.skipJansi", "false"); // enable Log4J's Jansi support
        LOGGER = LoggerFactory.getLogger("textbox-squared/Bootstrap");
    }

    public static void initPlugins() {
        PluginAPIImpl.set();

        LOGGER.info("Initializing plugin manager...");
        var pluginManager = new DefaultPluginManager(Paths.get("plugins"));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        for (var provider : pluginManager.getExtensions(SceneRendererProvider.class)) {
            SceneRendererProviderRegistry.register(provider);
        }
        PluginAPIImpl.INSTANCE.executePostLoadListeners(pluginManager);
    }
}
