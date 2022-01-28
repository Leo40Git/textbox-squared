package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.definition.FromDefinition;

public final class Face implements FromDefinition {
    public static final Face BLANK = new Face(Definition.builtin(), "None",
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), (name, image) -> new ImageIcon());

    private final Definition sourceDefinition;
    private final String name;
    private final BufferedImage image;
    private final FaceIconProvider iconProvider;
    private ImageIcon icon;

    public Face(Definition sourceDefinition, String name, BufferedImage image, FaceIconProvider iconProvider) {
        this.sourceDefinition = sourceDefinition;
        this.name = name;
        this.image = image;
        this.iconProvider = iconProvider;
    }

    public boolean isBlank() {
        return this == BLANK;
    }

    @Override
    public Definition getSourceDefinition() {
        return sourceDefinition;
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
