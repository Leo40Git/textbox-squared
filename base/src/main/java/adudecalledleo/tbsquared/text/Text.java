package adudecalledleo.tbsquared.text;

import java.util.List;
import java.util.Optional;

public interface Text {
    TextStyle getStyle();
    String getContents();
    List<Text> getChildren();
    <T, R> Optional<R> visitSelf(TextVisitor<T, R> visitor, T data);

    default <T, R> Optional<R> visit(TextVisitor<T, R> visitor, T data) {
        var result = visitSelf(visitor, data);
        if (result.isPresent()) {
            return result;
        }
        for (var child : getChildren()) {
            result = child.visit(visitor, data);
            if (result.isPresent()) {
                return result;
            }
        }
        return result;
    }
}
