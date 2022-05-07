package adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.app.plugin.api.util.ImageLoader;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.DefaultFacePool;
import adudecalledleo.tbsquared.face.FaceCategory;
import adudecalledleo.tbsquared.face.FacePool;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;

public final class FacePoolRecipe {
    @JsonValue
    public final Map<String, FaceCategoryRecipe> categories;

    public FacePoolRecipe() {
        this.categories = new LinkedHashMap<>();
    }

    @JsonAnySetter
    public FacePoolRecipe addCategory(String name, FaceCategoryRecipe category) {
        this.categories.put(name, category);
        return this;
    }

    public FacePool make(Definition sourceDefinition, ImageLoader imgLoader) throws FaceRecipeException {
        List<FaceCategory> builtCats = new LinkedList<>();
        for (var entry : this.categories.entrySet()) {
            var name = entry.getKey();
            try {
                builtCats.add(entry.getValue().make(name, sourceDefinition, imgLoader));
            } catch (FaceRecipeException e) {
                throw new FaceRecipeException("Failed to build category \"%s\"".formatted(name), e);
            }
        }
        return new DefaultFacePool(builtCats);
    }

    @Override
    public String toString() {
        return "FacePoolRecipe{" +
                "categories=" + categories +
                '}';
    }
}
