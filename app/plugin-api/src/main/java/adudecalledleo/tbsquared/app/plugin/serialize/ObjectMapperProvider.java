package adudecalledleo.tbsquared.app.plugin.serialize;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@FunctionalInterface
public interface ObjectMapperProvider {
    ObjectMapper getObjectMapper();

    default <T> T readValue(InputStream src, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(src, valueType);
    }

    default <T> T readValue(InputStream src, JavaType valueType) throws IOException {
        return getObjectMapper().readValue(src, valueType);
    }
}
