package adudecalledleo.tbsquared.app.plugin.api.util;

import java.awt.image.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public record ResourceImageLoader(Class<?> delegate, String basePath) implements ImageLoader {
    @Override
    public BufferedImage loadImage(String path) throws IOException {
        String fullPath = basePath + path;
        var in = delegate.getResourceAsStream(fullPath);
        if (in == null) {
            throw new FileNotFoundException(fullPath);
        } else try (in) {
            return ImageIO.read(in);
        }
    }
}
