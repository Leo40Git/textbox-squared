package adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face;

import java.awt.image.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;

public record FaceRecipe(String path, FaceIconProvider iconProvider, String comment) {
    public FaceRecipe(String path, FaceIconProvider iconProvider) {
        this(path, iconProvider, "");
    }

    public Face make(String name, Definition sourceDefinition, Path basePath)
            throws FaceRecipeException {
        Path fullPath = basePath.resolve(this.path);
        BufferedImage image;
        try (var in = Files.newInputStream(fullPath)) {
            image = ImageIO.read(in);
        } catch (IOException e) {
            throw new FaceRecipeException("Failed to load image file for face \"%s\" from \"%s\"".formatted(name, fullPath), e);
        }
        return new Face(sourceDefinition, name, image, this.iconProvider, this.comment);
    }
}
