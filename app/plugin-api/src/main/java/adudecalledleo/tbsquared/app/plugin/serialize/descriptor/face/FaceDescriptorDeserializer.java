package adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

final class FaceDescriptorDeserializer extends JsonDeserializer<FaceDescriptor> {
    public static final class FaceDescObject {
        public String path, comment;
    }

    @Override
    public FaceDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.currentTokenId()) {
        case JsonTokenId.ID_STRING -> {
            return new FaceDescriptor(p.getText(), "");
        }
        case JsonTokenId.ID_START_OBJECT -> {
            var obj = ctxt.readValue(p, FaceDescObject.class);
            return new FaceDescriptor(obj.path, obj.comment);
        }
        }
        try {
            return (FaceDescriptor) ctxt.handleUnexpectedToken(FaceDescriptor.class, p.currentToken(), p,
                    "Unexpected token (%s), expected %s or %s for FaceDescriptor value",
                    p.currentToken(), JsonToken.VALUE_STRING, JsonToken.START_OBJECT);
        } catch (JsonMappingException e) {
            throw e;
        } catch (IOException e) {
            throw JsonMappingException.fromUnexpectedIOE(e);
        }
    }
}
