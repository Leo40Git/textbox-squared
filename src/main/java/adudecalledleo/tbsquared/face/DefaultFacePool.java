package adudecalledleo.tbsquared.face;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultFacePool implements FacePool {
    protected final Map<String, FaceCategory> categories;

    public DefaultFacePool(Collection<FaceCategory> categories) {
        Map<String, FaceCategory> catMap = new LinkedHashMap<>(categories.size());
        for (var cat : categories) {
            catMap.put(cat.getName(), cat);
        }
        this.categories = Map.copyOf(catMap);
    }

    public DefaultFacePool(FaceCategory... categories) {
        Map<String, FaceCategory> catMap = new LinkedHashMap<>(categories.length);
        for (var cat : categories) {
            catMap.put(cat.getName(), cat);
        }
        this.categories = Map.copyOf(catMap);
    }

    @Override
    public Collection<FaceCategory> getCategories() {
        return categories.values();
    }

    @Override
    public FaceCategory getCategory(String name) {
        var cat = categories.get(name);
        if (cat == null) {
            throw new IllegalArgumentException("Unknown category name \"%s\"".formatted(name));
        }
        return cat;
    }
}
