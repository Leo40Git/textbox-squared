package adudecalledleo.tbsquared.app.plugin.renderer;

import adudecalledleo.tbsquared.app.plugin.config.ConfigSpec;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import org.pf4j.ExtensionPoint;

public interface SceneRendererProvider extends ExtensionPoint {
    String getId();
    ConfigSpec getConfigSpec();
    SceneRenderer createSceneRenderer(DataTracker config);
}
