package adudecalledleo.tbsquared.app.plugin;

import java.io.IOException;
import java.io.InputStream;

import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;

public interface PluginAPI {
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
