package adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face;

import java.util.LinkedHashMap;
import java.util.Map;

import adudecalledleo.tbsquared.app.plugin.api.util.ImageLoader;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.FaceCategory;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.Nullable;

@JsonPropertyOrder({ "icon", "faces" })
public final class FaceCategoryRecipe {
    public final Map<String, FaceRecipe> faces;
    public @Nullable String icon;

    public FaceCategoryRecipe() {
        this.faces = new LinkedHashMap<>();
        this.icon = null;
    }

    public FaceCategoryRecipe addFace(String name, FaceRecipe face) {
        this.faces.put(name, face);
        return this;
    }

    public FaceCategoryRecipe setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public FaceCategory make(String name, Definition sourceDefinition, ImageLoader imgLoader)
            throws FaceRecipeException {
        var builder = FaceCategory.builder(name).iconFace(this.icon);
        for (var entry : this.faces.entrySet()) {
            builder.addFace(entry.getValue().make(entry.getKey(), sourceDefinition, imgLoader));
        }
        return builder.build();
    }

    @Override
    public String toString() {
        return "FaceCategoryRecipe{" +
                "faces=" + faces +
                ", icon='" + icon + '\'' +
                '}';
    }
}
