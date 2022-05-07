package adudecalledleo.tbsquared.app.plugin.api.util;

import java.awt.image.*;
import java.io.IOException;

@FunctionalInterface
public interface ImageLoader {
    BufferedImage loadImage(String path) throws IOException;
}
