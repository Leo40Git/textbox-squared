package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.util.render.HorizontalAlignment;
import adudecalledleo.tbsquared.util.render.VerticalAlignment;

public record BackgroundImageFactory(BufferedImage backgroundImage, Color backgroundColor,
                                     HorizontalAlignment alignX, VerticalAlignment alignY) implements ImageFactory {
    @Override
    public BufferedImage createImage(int width, int height) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        g.setBackground(backgroundColor);
        g.clearRect(0, 0, width, height);
        g.drawImage(backgroundImage,
                alignX.align(width, backgroundImage.getWidth()),
                alignY.align(height, backgroundImage.getHeight()), null);
        g.dispose();
        return image;
    }
}
