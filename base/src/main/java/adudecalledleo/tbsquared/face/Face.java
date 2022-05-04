package adudecalledleo.tbsquared.face;

import java.awt.image.*;

import javax.swing.*;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.definition.FromDefinition;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;
import org.jetbrains.annotations.Nullable;

public final class Face implements FromDefinition {
    public static final Face BLANK = new Face(Definition.builtin(), "None",
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), FaceIconProvider.getNone());

    private final Definition sourceDefinition;
    private final String name;
    private final BufferedImage image;
    private final FaceIconProvider iconProvider;
    private final String comment;
    private ImageIcon icon;
    private boolean iconComputed;

    public Face(Definition sourceDefinition, String name, BufferedImage image, FaceIconProvider iconProvider, String comment) {
        this.sourceDefinition = sourceDefinition;
        this.name = name;
        this.image = image;
        this.iconProvider = iconProvider;
        this.comment = comment;
    }

    public Face(Definition sourceDefinition, String name, BufferedImage image, FaceIconProvider iconProvider) {
        this(sourceDefinition, name, image, iconProvider, "");
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

    public String getComment() {
        return comment;
    }

    public @Nullable ImageIcon getIcon() {
        if (!iconComputed) {
            icon = iconProvider.createIcon(name, image);
            iconComputed = true;
        }
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
