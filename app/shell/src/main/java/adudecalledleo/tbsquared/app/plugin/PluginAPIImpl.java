package adudecalledleo.tbsquared.app.plugin;

import adudecalledleo.tbsquared.app.Main;
import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;

public final class PluginAPIImpl implements PluginAPI {
    public static final PluginAPI INSTANCE = new PluginAPIImpl();

    private PluginAPIImpl() { }

    @Override
    public Version getVersion() {
        return Main.PLUGIN_API_VERSION;
    }

    @Override
    public void registerSceneRendererProvider(SceneRendererProvider provider) {

    }

    @Override
    public ObjectMapper getObjectMapper() {
        return Main.JACKSON;
    }
}
