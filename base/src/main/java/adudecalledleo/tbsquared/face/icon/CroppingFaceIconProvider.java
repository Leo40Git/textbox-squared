package adudecalledleo.tbsquared.face.icon;

import java.awt.image.*;

import javax.swing.*;

import adudecalledleo.tbsquared.util.render.Colors;
import org.jetbrains.annotations.NotNull;

public record CroppingFaceIconProvider(int x, int y, int width, int height) implements FaceIconProvider {
    @Override
    public @NotNull ImageIcon createIcon(String name, BufferedImage image) {
        var croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g = croppedImage.createGraphics();
        g.setBackground(Colors.TRANSPARENT);
        g.clearRect(0, 0, width, height);
        g.drawImage(image, 0, 0, width, height, x, y, width, height, null);
        g.dispose();
        return new ImageIcon(croppedImage, name);
    }
}
