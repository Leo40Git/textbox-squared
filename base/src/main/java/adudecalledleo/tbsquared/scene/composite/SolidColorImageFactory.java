package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.awt.image.*;

public record SolidColorImageFactory(Color color) implements ImageFactory {
    @Override
    public BufferedImage createImage(int width, int height) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        g.setBackground(color);
        g.clearRect(0, 0, width, height);
        g.dispose();
        return image;
    }
}
