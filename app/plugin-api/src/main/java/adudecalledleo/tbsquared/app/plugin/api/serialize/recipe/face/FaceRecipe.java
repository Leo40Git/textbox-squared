package adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face;

import java.awt.image.*;
import java.io.IOException;

import adudecalledleo.tbsquared.app.plugin.api.util.ImageLoader;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;

public record FaceRecipe(String path, FaceIconProvider iconProvider, String comment) {
    public FaceRecipe(String path, FaceIconProvider iconProvider) {
        this(path, iconProvider, "");
    }

    public Face make(String name, Definition sourceDefinition, ImageLoader imgLoader)
            throws FaceRecipeException {
        BufferedImage image;
        try {
            image = imgLoader.loadImage(path);
        } catch (IOException e) {
            throw new FaceRecipeException("Failed to load image file for face \"%s\" from \"%s\"".formatted(name, path), e);
        }
        return new Face(sourceDefinition, name, image, this.iconProvider, this.comment);
    }
}
