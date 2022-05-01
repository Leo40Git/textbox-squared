package adudecalledleo.tbsquared.face;

import java.util.Collection;

public interface FacePool {
    static FacePool empty() {
        return EmptyFacePool.INSTANCE;
    }

    Collection<FaceCategory> getCategories();

    default FaceCategory getCategory(String name) {
        for (var cat : getCategories()) {
            if (name.equals(cat.getName())) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Unknown category name \"%s\"".formatted(name));
    }

    default FaceCategory getCategoryOf(Face face) {
        for (var cat : getCategories()) {
            if (cat.getFaces().contains(face)) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Couldn't find face \"%s\" in any categories".formatted(face.getName()));
    }

    default String getFacePath(Face face) {
        return getCategoryOf(face) + "/" + face.getName();
    }

    default Face getFaceByPath(String path) {
        int index = path.indexOf('/');
        if (index < 0) {
            throw new IllegalArgumentException("Path \"%s\" is invalid: No separator between category and name".formatted(path));
        }
        return getCategory(path.substring(0, index)).getFace(path.substring(index + 1));
    }
}
