package adudecalledleo.tbsquared.render.composite.impl;

import java.awt.image.*;

import adudecalledleo.tbsquared.render.composite.SceneImageFactory;

public final class DefaultSceneImageFactory implements SceneImageFactory {
    @Override
    public BufferedImage createSceneImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
