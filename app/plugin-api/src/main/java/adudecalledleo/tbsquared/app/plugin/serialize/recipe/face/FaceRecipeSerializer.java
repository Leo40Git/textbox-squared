package adudecalledleo.tbsquared.app.plugin.serialize.recipe.face;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

final class FaceRecipeSerializer extends JsonSerializer<FaceRecipe> {
    @Override
    public void serialize(FaceRecipe value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.comment().isEmpty()) {
            gen.writeString(value.path());
        } else {
            gen.writeStartObject();
            gen.writeFieldName("path");
            gen.writeString(value.path());
            gen.writeFieldName("comment");
            gen.writeString(value.comment());
            gen.writeEndObject();
        }
    }
}
