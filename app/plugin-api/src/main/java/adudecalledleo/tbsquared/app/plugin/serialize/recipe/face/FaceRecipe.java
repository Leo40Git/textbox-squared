package adudecalledleo.tbsquared.app.plugin.serialize.recipe.face;

import java.awt.image.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.FaceIconProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = FaceRecipeSerializer.class)
@JsonDeserialize(using = FaceRecipeDeserializer.class)
public record FaceRecipe(String path, String comment) {
    public FaceRecipe(String path) {
        this(path, "");
    }

    public Face make(String name, Definition sourceDefinition, Path basePath, FaceIconProvider iconProvider)
            throws FaceRecipeException {
        Path fullPath = basePath.resolve(this.path);
        BufferedImage image;
        try (var in = Files.newInputStream(fullPath)) {
            image = ImageIO.read(in);
        } catch (IOException e) {
            throw new FaceRecipeException("Failed to load image file for face \"%s\" from \"%s\"".formatted(name, fullPath), e);
        }
        return new Face(sourceDefinition, name, image, iconProvider, this.comment);
    }
}
