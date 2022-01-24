package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

public final class Face {
    public static final Face BLANK = new Face("None",
            new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB), (name, image) -> new ImageIcon());

    private final String name;
    private final BufferedImage image;
    private final FaceIconProvider iconProvider;
    private ImageIcon icon;

    public Face(String name, BufferedImage image, FaceIconProvider iconProvider) {
        this.name = name;
        this.image = image;
        this.iconProvider = iconProvider;
    }

    public boolean isBlank() {
        return this == BLANK;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public ImageIcon getIcon() {
        if (icon == null) {
            icon = iconProvider.createIcon(name, image);
        }
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
