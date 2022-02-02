package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface FaceIconProvider {
    FaceIconProvider NONE = (name, image) -> null;

    @Nullable ImageIcon createIcon(String name, BufferedImage image);
}
