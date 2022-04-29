package adudecalledleo.tbsquared.parse.node;

import java.util.LinkedList;
import java.util.List;

public abstract class ContainerNode extends Node {
    protected final List<Node> children;

    public ContainerNode(String name, List<Node> children) {
        super(name);
        this.children = new LinkedList<>(children);
    }

    public ContainerNode(String name) {
        super(name);
        this.children = new LinkedList<>();
    }

    public List<Node> getChildren() {
        return children;
    }
}
