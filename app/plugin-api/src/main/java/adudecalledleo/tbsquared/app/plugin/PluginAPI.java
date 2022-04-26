package adudecalledleo.tbsquared.app.plugin;

import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProviderRegistry;
import com.github.zafarkhaja.semver.Version;

public interface PluginAPI extends SceneRendererProviderRegistry {
    Version getVersion();
}
