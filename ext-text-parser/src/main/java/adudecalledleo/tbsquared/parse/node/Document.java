package adudecalledleo.tbsquared.parse.node;

import java.util.ArrayList;
import java.util.List;

public final class Document extends Node {
    public static final Document EMPTY = new Document(List.of());

    public Document(List<Node> children) {
        super(children);
    }

    public Document() {
        super(new ArrayList<>());
    }
}
