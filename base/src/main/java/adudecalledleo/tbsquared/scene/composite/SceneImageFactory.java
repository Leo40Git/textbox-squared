package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

@FunctionalInterface
public interface SceneImageFactory {
    BufferedImage createSceneImage(int width, int height);
}
