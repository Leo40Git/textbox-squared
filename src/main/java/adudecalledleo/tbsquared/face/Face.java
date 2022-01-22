package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

public record Face(String name, BufferedImage image, ImageIcon icon) {
    public static final Face BLANK = new Face("None",
            new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB), new ImageIcon());

    public boolean isBlank() {
        return this == BLANK;
    }
}
