package adudecalledleo.tbsquared.app.plugin.api.util;

import java.awt.image.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public record NIOImageLoader(Path basePath) implements ImageLoader {
    @Override
    public BufferedImage loadImage(String path) throws IOException {
        Path fullPath = basePath.resolve(path);
        try (var in = Files.newInputStream(fullPath)) {
            return ImageIO.read(in);
        }
    }
}
