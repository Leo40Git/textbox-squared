package adudecalledleo.tbsquared.parse.node;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractContainerNode extends AbstractNode implements ContainerNode {
    protected final List<Node> children;

    public AbstractContainerNode(String name, List<Node> children) {
        super(name);
        this.children = new LinkedList<>(children);
    }

    public AbstractContainerNode(String name) {
        super(name);
        this.children = new LinkedList<>();
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public <T, R> Optional<R> visit(NodeVisitor<T, R> visitor, T data) {
        var result = visitSelf(visitor, data);
        if (result.isPresent()) {
            return result;
        }
        for (var child : children) {
            result = child.visit(visitor, data);
            if (result.isPresent()) {
                return result;
            }
        }
        return result;
    }
}
