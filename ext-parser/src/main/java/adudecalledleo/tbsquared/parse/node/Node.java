package adudecalledleo.tbsquared.parse.node;

import java.util.Optional;

public interface Node {
    String getName();

    default <T, R> Optional<R> visitSelf(NodeVisitor<T, R> visitor, T data) {
        return visitor.visit(this, data);
    }

    default <T, R> Optional<R> visit(NodeVisitor<T, R> visitor, T data) {
        return visitSelf(visitor, data);
    }
}
