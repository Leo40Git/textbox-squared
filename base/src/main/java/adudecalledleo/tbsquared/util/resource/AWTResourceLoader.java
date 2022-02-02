package adudecalledleo.tbsquared.util.resource;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;

// separate class to prevent possible AWT init shenanigans
public class AWTResourceLoader extends ResourceLoader {
    public AWTResourceLoader(Class<?> delegate) {
        super(delegate);
    }

    public BufferedImage loadImage(String path) throws IOException {
        try (var in = newInputStream(path)) {
            return ImageIO.read(in);
        }
    }

    public Font loadFont(int format, String path) throws IOException, FontFormatException {
        try (var in = newInputStream(path)) {
            return Font.createFont(format, in);
        }
    }
}
