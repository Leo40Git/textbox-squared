package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;

import adudecalledleo.tbsquared.util.render.Colors;
import adudecalledleo.tbsquared.util.shape.Dim;

/**
 * Creates the base image a {@link CompositeSceneRenderer} will draw to.
 *
 * <p>This factory is responsible for the <em>background</em> of all scenes rendered.
 */
@FunctionalInterface
public interface ImageFactory {
    /**
     * This factory will create completely blank (transparent) images.
     */
    ImageFactory BLANK = new SolidColorImageFactory(Colors.TRANSPARENT);

    /**
     * Creates an image with the specified dimensions.
     *
     * @param width image width
     * @param height image height
     * @return newly created image
     *
     * @implNote Since the returned image will be modified, this should <em>never</em> return the same image twice!
     */
    BufferedImage createImage(int width, int height);

    /**
     * Creates an image with the specified dimensions.
     *
     * @param dim image dimensions
     * @return newly created image
     */
    default BufferedImage createImage(Dim dim) {
        return createImage(dim.width(), dim.height());
    }
}
