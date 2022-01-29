package adudecalledleo.tbsquared.scene.composite.impl;

import java.awt.image.*;

import adudecalledleo.tbsquared.scene.composite.SceneImageFactory;

public final class DefaultSceneImageFactory implements SceneImageFactory {
    public static final SceneImageFactory INSTANCE = new DefaultSceneImageFactory();

    private DefaultSceneImageFactory() { }

    @Override
    public BufferedImage createSceneImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
