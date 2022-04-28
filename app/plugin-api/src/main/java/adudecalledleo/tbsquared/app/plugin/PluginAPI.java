package adudecalledleo.tbsquared.app.plugin;

import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProviderRegistry;
import adudecalledleo.tbsquared.app.plugin.serialize.ObjectMapperProvider;
import com.github.zafarkhaja.semver.Version;

public interface PluginAPI extends SceneRendererProviderRegistry, ObjectMapperProvider {
    Version getVersion();
}
