package adudecalledleo.tbsquared.face;

import java.util.Collection;
import java.util.List;

final class EmptyFacePool implements FacePool {
    public static final FacePool INSTANCE = new EmptyFacePool();

    private EmptyFacePool() { }

    @Override
    public Collection<FaceCategory> getCategories() {
        return List.of();
    }
}
