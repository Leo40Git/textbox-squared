package adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.FaceCategory;
import adudecalledleo.tbsquared.face.FaceIconProvider;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "icon", "faces" })
public final class FaceCategoryDescriptor {
    public final Map<String, FaceDescriptor> faces;
    public String icon;

    public FaceCategoryDescriptor() {
        this.faces = new LinkedHashMap<>();
        this.icon = null;
    }

    public FaceCategory build(String name, Definition sourceDefinition, Path basePath, FaceIconProvider iconProvider)
            throws FacePoolBuildException {
        var builder = FaceCategory.builder(name).iconFace(this.icon);
        for (var entry : this.faces.entrySet()) {
            builder.addFace(entry.getValue().build(entry.getKey(), sourceDefinition, basePath, iconProvider));
        }
        return builder.build();
    }

    @Override
    public String toString() {
        return "FaceCategoryDescriptor{" +
                "faces=" + faces +
                ", icon='" + icon + '\'' +
                '}';
    }
}
