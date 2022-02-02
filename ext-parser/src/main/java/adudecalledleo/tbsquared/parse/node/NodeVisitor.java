package adudecalledleo.tbsquared.parse.node;

import java.util.Optional;

public interface NodeVisitor<T, R> {
    Optional<R> visit(Node node, T data);
}
