package adudecalledleo.tbsquared.app;

import java.nio.file.Paths;

import adudecalledleo.tbsquared.app.plugin.PluginAPIImpl;
import adudecalledleo.tbsquared.app.plugin.SceneRendererProviderRegistry;
import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.serialize.module.JSemVerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.zafarkhaja.semver.Version;
import org.pf4j.DefaultPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private Main() { }

    public static final Version VERSION = Version.forIntegers(1, 0, 0);
    public static final Version PLUGIN_API_VERSION = Version.forIntegers(1, 0, 0);

    public static final ObjectMapper JACKSON = new ObjectMapper()
            .registerModules(
                    new ParameterNamesModule(),
                    new Jdk8Module(),
                    new JavaTimeModule(),
                    new JSemVerModule());

    public static final Logger LOGGER;

    static {
        System.setProperty("log4j.skipJansi", "false"); // enable Log4J's Jansi support
        LOGGER = LoggerFactory.getLogger("textbox-squared");
    }

    public static void main(String[] args) {
        PluginAPIImpl.set();

        LOGGER.info("Initializing plugin manager...");
        var pluginManager = new DefaultPluginManager(Paths.get("plugins"));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        for (var provider : pluginManager.getExtensions(SceneRendererProvider.class)) {
            SceneRendererProviderRegistry.register(provider);
        }
    }
}
