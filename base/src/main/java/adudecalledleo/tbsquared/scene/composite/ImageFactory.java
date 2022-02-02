package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

@FunctionalInterface
public interface ImageFactory {
    ImageFactory DEFAULT = DefaultImageFactory.INSTANCE;

    BufferedImage createImage(int width, int height);
}
