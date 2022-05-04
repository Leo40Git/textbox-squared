package adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face;

import java.util.HashMap;
import java.util.Map;

import adudecalledleo.tbsquared.face.icon.CroppingFaceIconProvider;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;
import adudecalledleo.tbsquared.face.icon.ScalingFaceIconProvider;
import org.jetbrains.annotations.Nullable;

public final class FaceIconProviders {
    private FaceIconProviders() { }

    private static final Map<String, Class<? extends FaceIconProvider>> TYPES = new HashMap<>();
    private static final Map<Class<? extends FaceIconProvider>, String> TYPE_NAMES = new HashMap<>();

    public static void register(String typeName, Class<? extends FaceIconProvider> type) {
        if (TYPES.containsKey(typeName)) {
            throw new IllegalArgumentException("Type with name \"" + typeName + "\" already registered!");
        }
        if (TYPE_NAMES.containsKey(type)) {
            throw new IllegalArgumentException("Type \"" + type.getName() + "\" already registered!");
        }
        TYPES.put(typeName, type);
        TYPE_NAMES.put(type, typeName);
    }

    public static @Nullable Class<? extends FaceIconProvider> getType(String typeName) {
        return TYPES.get(typeName);
    }

    public static @Nullable String getTypeName(Class<? extends FaceIconProvider> type) {
        return TYPE_NAMES.get(type);
    }

    static {
        register("crop", CroppingFaceIconProvider.class);
        register("scale", ScalingFaceIconProvider.class);
    }
}
