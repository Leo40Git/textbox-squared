package adudecalledleo.tbsquared.util.render;

import java.awt.image.*;
import java.util.List;

import adudecalledleo.tbsquared.scene.composite.ImageFactory;

public final class BufferedImages {
    private BufferedImages() { }

    public static BufferedImage stitch(List<BufferedImage> images, Orientation orientation,
                                       HorizontalAlignment alignX, VerticalAlignment alignY,
                                       ImageFactory finalImageFactory) {
        final int imageCount = images.size();
        int maxW = 0, maxH = 0;
        for (BufferedImage image : images) {
            maxW = Math.max(maxW, image.getWidth());
            maxH = Math.max(maxH, image.getHeight());
        }
        int finalW = 0, finalH = 0;
        if (orientation.isVertical()) {
            finalH = maxH * imageCount;
        } else {
            finalW = maxH * imageCount;
        }

        var finalImage = finalImageFactory.createImage(finalW, finalH);
        var g = finalImage.createGraphics();
        int x = 0, y = 0;
        int i = 0;
        if (orientation.getIncrement() < 0) {
            i = imageCount - 1;
        }
        for (; i > 0 && i < imageCount; i += orientation.getIncrement()) {
            var image = images.get(i);
            g.drawImage(images.get(i), alignX.align(maxW, image.getWidth()), alignY.align(maxH, image.getHeight()),
                    null);
            if (orientation.isVertical()) {
                y += maxH;
            } else {
                x += maxW;
            }
        }
        g.dispose();
        return finalImage;
    }
}
