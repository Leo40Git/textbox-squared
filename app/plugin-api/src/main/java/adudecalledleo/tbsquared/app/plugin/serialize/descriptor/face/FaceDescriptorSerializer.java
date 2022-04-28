package adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

final class FaceDescriptorSerializer extends JsonSerializer<FaceDescriptor> {
    @Override
    public void serialize(FaceDescriptor value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
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
