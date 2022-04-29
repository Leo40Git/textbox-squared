package adudecalledleo.tbsquared.app.plugin.api;

import java.io.IOException;
import java.io.InputStream;

import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.impl.PluginAPIHolder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;

public interface PluginAPI {
    static PluginAPI get() {
        var api = PluginAPIHolder.getInstance();
        if (api == null) {
            throw new IllegalStateException("No PluginAPI instance configured!");
        }
        return api;
    }

    Version getVersion();

    ObjectMapper getObjectMapper();

    default <T> T readValue(InputStream src, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(src, valueType);
    }

    default <T> T readValue(InputStream src, JavaType valueType) throws IOException {
        return getObjectMapper().readValue(src, valueType);
    }

    void registerSceneRendererProvider(SceneRendererProvider provider);
}
