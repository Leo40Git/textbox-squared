package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

final class DefaultSceneImageFactory implements SceneImageFactory {
    static final SceneImageFactory INSTANCE = new DefaultSceneImageFactory();

    private DefaultSceneImageFactory() { }

    @Override
    public BufferedImage createSceneImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
