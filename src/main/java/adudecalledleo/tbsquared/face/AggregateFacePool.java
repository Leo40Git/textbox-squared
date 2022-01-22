package adudecalledleo.tbsquared.face;

import java.util.*;

public final class AggregateFacePool implements FacePool {
    private final List<FaceCategory> categories;

    public AggregateFacePool(FacePool... pools) {
        record CombinedCategory(String name, List<Face> faces, String iconFaceName) {
            public FaceCategory toFinal() {
                return new FaceCategory(name, faces, iconFaceName);
            }
        }

        Map<String, CombinedCategory> combinedCategoryMap = new HashMap<>();
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

        categories = Arrays.asList(combinedCategoryMap.values().stream().map(CombinedCategory::toFinal).toArray(FaceCategory[]::new));
    }

    @Override
    public List<FaceCategory> getCategories() {
        return categories;
    }
}
