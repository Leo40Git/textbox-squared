package adudecalledleo.tbsquared.app.plugin.impl;

import adudecalledleo.tbsquared.app.Main;
import adudecalledleo.tbsquared.app.plugin.api.PluginAPI;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.impl.renderer.SceneRendererProviderRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;

public final class PluginAPIImpl implements PluginAPI {
    public static final PluginAPI INSTANCE = new PluginAPIImpl();

    public static void set() {
        PluginAPIHolder.setInstance(INSTANCE);
    }

    @Override
    public Version getVersion() {
        return Main.PLUGIN_API_VERSION;
    }

    @Override
    public void registerSceneRendererProvider(SceneRendererProvider provider) {
        SceneRendererProviderRegistry.register(provider);
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return Main.JACKSON;
    }
}
