package adudecalledleo.tbsquared.parse.node;

import java.util.List;

public final class Document extends AbstractContainerNode {
    public static final String NAME = "doc";

    public Document(List<Node> children) {
        super(NAME, children);
    }

    public Document() {
        super(NAME);
    }
}
