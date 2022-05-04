package adudecalledleo.tbsquared.face.icon;

import java.awt.image.*;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

final class NoFaceIconProvider implements FaceIconProvider {
    public static final FaceIconProvider INSTANCE = new NoFaceIconProvider();

    private NoFaceIconProvider() { }

    @Override
    public @Nullable ImageIcon createIcon(String name, BufferedImage image) {
        return null;
    }

    @Override
    public String toString() {
        return "NoFaceIconProvider";
    }
}
