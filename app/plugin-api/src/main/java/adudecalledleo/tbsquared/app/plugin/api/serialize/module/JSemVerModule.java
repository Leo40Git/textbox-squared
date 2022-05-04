package adudecalledleo.tbsquared.app.plugin.api.serialize.module;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;

public final class JSemVerModule extends SimpleModule {
    public JSemVerModule() {
        addDeserializer(Version.class, VersionDeserializer.INSTANCE);
        addSerializer(Version.class, VersionSerializer.INSTANCE);
    }

    private static final class VersionDeserializer extends JsonDeserializer<Version> {
        public static final VersionDeserializer INSTANCE = new VersionDeserializer();

        @Override
        public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentTokenId() == JsonTokenId.ID_STRING) {
                String text;
                try {
                    text = p.getText();
                } catch (IOException e) {
                    throw JsonMappingException.fromUnexpectedIOE(e);
                }
                try {
                    return Version.valueOf(text);
                } catch (IllegalArgumentException | ParseException e) {
                    try {
                        return (Version) ctxt.handleWeirdStringValue(Version.class, text,
                                "Failed to deserialize version: (%s) %s",
                                e.getClass().getName(), e.getMessage());
                    } catch (JsonMappingException ex) {
                        throw ex;
                    } catch (IOException ex) {
                        throw JsonMappingException.fromUnexpectedIOE(ex);
                    }
                }
            } else {
                try {
                    return (Version) ctxt.handleUnexpectedToken(Version.class, p.currentToken(), p,
                            "Unexpected token (%s), expected %s for Version value",
                            p.currentToken(), JsonToken.VALUE_STRING);
                } catch (JsonMappingException e) {
                    throw e;
                } catch (IOException e) {
                    throw JsonMappingException.fromUnexpectedIOE(e);
                }
            }
        }
    }

    private static final class VersionSerializer extends JsonSerializer<Version> {
        public static final VersionSerializer INSTANCE = new VersionSerializer();

        @Override
        public void serialize(Version value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
        }
    }
}
