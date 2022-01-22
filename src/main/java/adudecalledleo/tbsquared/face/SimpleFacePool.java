package adudecalledleo.tbsquared.face;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleFacePool implements FacePool {
    protected final List<FaceCategory> categories;

    public SimpleFacePool(Collection<FaceCategory> categories) {
        this.categories = List.copyOf(categories);
    }

    public SimpleFacePool(FaceCategory... categories) {
        this.categories = Arrays.asList(categories);
    }

    @Override
    public List<FaceCategory> getCategories() {
        return categories;
    }
}
