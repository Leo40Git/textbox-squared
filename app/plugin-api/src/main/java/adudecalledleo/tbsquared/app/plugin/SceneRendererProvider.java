package adudecalledleo.tbsquared.app.plugin;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.SceneRenderer;

public interface SceneRendererProvider {
    String getId();
    SceneRenderer createSceneRenderer(DataTracker config);
}
