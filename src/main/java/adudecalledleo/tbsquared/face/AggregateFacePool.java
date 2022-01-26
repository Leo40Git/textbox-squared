package adudecalledleo.tbsquared.face;

import java.util.*;

public final class AggregateFacePool implements FacePool {
    private final Map<String, FaceCategory> categories;

    public AggregateFacePool(FacePool... pools) {
        record CombinedCategory(String name, List<Face> faces, String iconFaceName) {
            public FaceCategory toFinal() {
                return new FaceCategory(name, faces, iconFaceName);
            }
        }

        Map<String, CombinedCategory> combinedCategoryMap = new LinkedHashMap<>();
        for (var pool : pools) {
            for (var cat : pool.getCategories()) {
                var cc = combinedCategoryMap.get(cat.getName());
                if (cc == null) {
                    cc = new CombinedCategory(
                            cat.getName(),
                            new ArrayList<>(cat.getFaces()),
                            cat.getIconFaceName());
                    combinedCategoryMap.put(cc.name(), cc);
                } else {
                    cc.faces().addAll(cat.getFaces());
                }
            }
        }

        categories = new LinkedHashMap<>();
        for (var entry : combinedCategoryMap.entrySet()) {
            categories.put(entry.getKey(), entry.getValue().toFinal());
        }
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
