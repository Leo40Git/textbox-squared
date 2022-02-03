package adudecalledleo.tbsquared.parse.node;

import java.util.LinkedList;
import java.util.List;

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
}
