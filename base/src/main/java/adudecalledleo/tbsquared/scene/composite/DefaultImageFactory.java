package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

final class DefaultImageFactory implements ImageFactory {
    static final ImageFactory INSTANCE = new DefaultImageFactory();

    private DefaultImageFactory() { }

    @Override
    public BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
