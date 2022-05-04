package adudecalledleo.tbsquared.face.icon;

import java.awt.image.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

final class DefaultFaceIconProvider implements FaceIconProvider {
    public static final FaceIconProvider INSTANCE = new DefaultFaceIconProvider();

    private DefaultFaceIconProvider() { }

    @Override
    public @NotNull ImageIcon createIcon(String name, BufferedImage image) {
        return new ImageIcon(image, name);
    }

    @Override
    public String toString() {
        return "DefaultFaceIconProvider";
    }
}
