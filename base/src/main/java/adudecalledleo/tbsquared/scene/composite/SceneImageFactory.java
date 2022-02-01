package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

@FunctionalInterface
public interface SceneImageFactory {
    static SceneImageFactory getDefault() {
        return DefaultSceneImageFactory.INSTANCE;
    }

    BufferedImage createSceneImage(int width, int height);
}
