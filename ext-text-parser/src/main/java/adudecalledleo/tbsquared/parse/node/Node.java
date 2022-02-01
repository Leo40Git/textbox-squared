package adudecalledleo.tbsquared.parse.node;

import java.util.List;

public sealed abstract class Node permits Document {
    protected final List<Node> children;

    public Node(List<Node> children) {
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }
}
