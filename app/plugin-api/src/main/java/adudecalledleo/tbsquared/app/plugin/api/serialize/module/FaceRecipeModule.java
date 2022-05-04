package adudecalledleo.tbsquared.app.plugin.api.serialize.module;

import java.io.IOException;

import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceRecipe;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class FaceRecipeModule extends SimpleModule {
    public FaceRecipeModule() {
        addDeserializer(FaceRecipe.class, FaceRecipeDeserializer.INSTANCE);
        addSerializer(FaceRecipe.class, FaceRecipeSerializer.INSTANCE);
        setMixInAnnotation(FaceIconProvider.class, FaceIconProviderAnnotationMixin.class);
    }

    private static final class FaceRecipeDeserializer extends JsonDeserializer<FaceRecipe> {
        public static final FaceRecipeDeserializer INSTANCE = new FaceRecipeDeserializer();

        public static final class Fro {
            public String path;
            public String comment = "";
            public FaceIconProvider icon = FaceIconProvider.getDefault();
        }

        @Override
        public FaceRecipe deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            switch (p.currentTokenId()) {
                case JsonTokenId.ID_STRING -> {
                    return new FaceRecipe(p.getText(), FaceIconProvider.getDefault());
                }
                case JsonTokenId.ID_START_OBJECT -> {
                    var obj = ctxt.readValue(p, Fro.class);
                    return new FaceRecipe(obj.path, obj.icon, obj.comment);
                }
            }
            try {
                return (FaceRecipe) ctxt.handleUnexpectedToken(FaceRecipe.class, p.currentToken(), p,
                        "Unexpected token (%s), expected %s or %s for FaceRecipe value",
                        p.currentToken(), JsonToken.VALUE_STRING, JsonToken.START_OBJECT);
            } catch (JsonMappingException e) {
                throw e;
            } catch (IOException e) {
                throw JsonMappingException.fromUnexpectedIOE(e);
            }
        }
    }

    private static final class FaceRecipeSerializer extends JsonSerializer<FaceRecipe> {
        public static final FaceRecipeSerializer INSTANCE = new FaceRecipeSerializer();

        @Override
        public void serialize(FaceRecipe value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.comment().isEmpty() && value.iconProvider() == FaceIconProvider.getDefault()) {
                gen.writeString(value.path());
            } else {
                gen.writeStartObject();
                gen.writeStringField("path", value.path());
                if (value.iconProvider() != FaceIconProvider.getDefault()) {
                    gen.writePOJOField("icon", value.iconProvider());
                }
                if (!value.comment().isEmpty()) {
                    gen.writeStringField("comment", value.comment());
                }
                gen.writeEndObject();
            }
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
    @JsonTypeIdResolver(FaceIconProviderTypeIdResolver.class)
    private static final class FaceIconProviderAnnotationMixin { }
}
