package adudecalledleo.tbsquared.face.icon;

import java.awt.image.*;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface FaceIconProvider {
    static FaceIconProvider getNone() {
        return NoFaceIconProvider.INSTANCE;
    }

    static FaceIconProvider getDefault() {
        return DefaultFaceIconProvider.INSTANCE;
    }

    @Nullable ImageIcon createIcon(String name, BufferedImage image);
}
