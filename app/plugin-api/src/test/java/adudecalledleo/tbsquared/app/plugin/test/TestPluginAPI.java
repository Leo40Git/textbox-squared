package adudecalledleo.tbsquared.app.plugin.test;

import adudecalledleo.tbsquared.app.plugin.api.PluginAPI;
import adudecalledleo.tbsquared.app.plugin.api.PostLoadListener;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.api.serialize.module.FaceRecipeModule;
import adudecalledleo.tbsquared.app.plugin.api.serialize.module.JSemVerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.zafarkhaja.semver.Version;

public final class TestPluginAPI implements PluginAPI {
    public static final Version VERSION = Version.valueOf("0.0.0+test");
    public static final ObjectMapper JACKSON = new ObjectMapper()
            .registerModules(
                    new ParameterNamesModule(),
                    new Jdk8Module(),
                    new JavaTimeModule(),
                    new FaceRecipeModule(),
                    new JSemVerModule());

    @Override
    public Version getVersion() {
        return VERSION;
    }

    @Override
    public void registerSceneRendererProvider(SceneRendererProvider provider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPostLoadListener(PostLoadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return JACKSON;
    }
}
