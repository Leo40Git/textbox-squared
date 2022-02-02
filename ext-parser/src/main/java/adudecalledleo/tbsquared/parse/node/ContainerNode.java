package adudecalledleo.tbsquared.parse.node;

import java.util.List;
import java.util.Optional;

public interface ContainerNode extends Node {
    List<Node> getChildren();
    <T, R> Optional<R> visit(NodeVisitor<T, R> visitor, T data);
}
