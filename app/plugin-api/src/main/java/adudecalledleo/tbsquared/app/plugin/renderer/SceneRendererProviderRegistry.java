package adudecalledleo.tbsquared.app.plugin.renderer;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface SceneRendererProviderRegistry {
    void registerSceneRendererProvider(SceneRendererProvider provider);
}
