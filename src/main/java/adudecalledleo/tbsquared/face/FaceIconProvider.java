package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

@FunctionalInterface
public interface FaceIconProvider {
    ImageIcon createIcon(String name, BufferedImage image);
}
