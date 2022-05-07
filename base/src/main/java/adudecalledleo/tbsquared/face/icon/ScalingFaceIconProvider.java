package adudecalledleo.tbsquared.face.icon;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import adudecalledleo.tbsquared.util.render.Colors;
import org.jetbrains.annotations.NotNull;

public record ScalingFaceIconProvider(double factor) implements FaceIconProvider {
    @Override
    public @NotNull ImageIcon createIcon(String name, BufferedImage image) {
        int newWidth = (int) Math.round(image.getWidth() * factor);
        int newHeight = (int) Math.round(image.getHeight() * factor);
        var scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        var g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setBackground(Colors.TRANSPARENT);
        g.clearRect(0, 0, newWidth, newHeight);
        g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        return new ImageIcon(scaledImage, name);
    }
}
