package adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.DefaultFacePool;
import adudecalledleo.tbsquared.face.FaceCategory;
import adudecalledleo.tbsquared.face.FaceIconProvider;
import adudecalledleo.tbsquared.face.FacePool;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;

public final class FacePoolDescriptor {
    @JsonValue
    public final Map<String, FaceCategoryDescriptor> categories;

    public FacePoolDescriptor() {
        this.categories = new LinkedHashMap<>();
    }

    @JsonAnySetter
    public void addCategory(String name, FaceCategoryDescriptor category) {
        this.categories.put(name, category);
    }

    public FacePool build(Definition sourceDefinition, Path basePath, FaceIconProvider iconProvider)
            throws FacePoolBuildException {
        List<FaceCategory> builtCats = new LinkedList<>();
        for (var entry : this.categories.entrySet()) {
            var name = entry.getKey();
            try {
                builtCats.add(entry.getValue().build(name, sourceDefinition, basePath, iconProvider));
            } catch (FacePoolBuildException e) {
                throw new FacePoolBuildException("Failed to build category \"%s\"".formatted(name), e);
            }
        }
        return new DefaultFacePool(builtCats);
    }

    @Override
    public String toString() {
        return "FacePoolDescriptor{" +
                "categories=" + categories +
                '}';
    }
}
