package adudecalledleo.tbsquared.app.plugin.impl.recipe.face;

import java.io.IOException;

import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceRecipe;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FaceRecipeDeserializer extends JsonDeserializer<FaceRecipe> {
    public static final class FaceRecipeObject {
        public String path, comment;
    }

    @Override
    public FaceRecipe deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.currentTokenId()) {
        case JsonTokenId.ID_STRING -> {
            return new FaceRecipe(p.getText());
        }
        case JsonTokenId.ID_START_OBJECT -> {
            var obj = ctxt.readValue(p, FaceRecipeObject.class);
            return new FaceRecipe(obj.path, obj.comment);
        }
        }
        try {
            return (FaceRecipe) ctxt.handleUnexpectedToken(FaceRecipe.class, p.currentToken(), p,
                    "Unexpected token (%s), expected %s or %s for FaceDescriptor value",
                    p.currentToken(), JsonToken.VALUE_STRING, JsonToken.START_OBJECT);
        } catch (JsonMappingException e) {
            throw e;
        } catch (IOException e) {
            throw JsonMappingException.fromUnexpectedIOE(e);
        }
    }
}
