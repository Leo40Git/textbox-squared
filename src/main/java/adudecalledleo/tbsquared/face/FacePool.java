package adudecalledleo.tbsquared.face;

import java.util.List;

public interface FacePool {
    List<FaceCategory> getCategories();

    default FaceCategory getCategory(String name) {
        for (var cat : getCategories()) {
            if (name.equals(cat.getName())) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Unknown category name \"%s\"".formatted(name));
    }
}
