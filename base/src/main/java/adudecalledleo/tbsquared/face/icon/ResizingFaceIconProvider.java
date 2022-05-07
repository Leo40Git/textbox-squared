package adudecalledleo.tbsquared.face.icon;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import adudecalledleo.tbsquared.util.render.Colors;
import org.jetbrains.annotations.NotNull;

public record ResizingFaceIconProvider(int width, int height) implements FaceIconProvider {
    static final FaceIconProvider DEFAULT = new ResizingFaceIconProvider(72, 72);

    @Override
    public @NotNull ImageIcon createIcon(String name, BufferedImage image) {
        var resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setBackground(Colors.TRANSPARENT);
        g.clearRect(0, 0, width, height);
        g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        return new ImageIcon(resizedImage, name);
    }
}
