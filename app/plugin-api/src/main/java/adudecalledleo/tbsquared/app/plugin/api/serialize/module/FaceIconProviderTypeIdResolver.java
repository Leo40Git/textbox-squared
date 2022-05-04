package adudecalledleo.tbsquared.app.plugin.api.serialize.module;

import java.io.IOException;

import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceIconProviders;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

public final class FaceIconProviderTypeIdResolver extends TypeIdResolverBase {
    @Override
    public String idFromValue(Object value) {
        if (value instanceof FaceIconProvider iconProvider) {
            var typeName = FaceIconProviders.getTypeName(iconProvider.getClass());
            if (typeName == null) {
                throw new IllegalArgumentException("Icon provider type \"" + iconProvider.getClass().getName() + "\" is unregistered!");
            }
            return typeName;
        }
        throw new IllegalArgumentException("Got non-icon provider value?!");
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        @SuppressWarnings("unchecked")
        var typeName = FaceIconProviders.getTypeName((Class<? extends FaceIconProvider>) suggestedType);
        if (typeName == null) {
            throw new IllegalArgumentException("Icon provider type \"" + suggestedType.getName() + "\" is unregistered!");
        }
        return typeName;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        var type = FaceIconProviders.getType(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown icon provider type \"" + id + "\"!");
        }
        return context.constructType(type);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
