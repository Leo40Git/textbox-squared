package adudecalledleo.tbsquared.parse.node;

import java.util.List;

public final class TextNode extends Node {
    private final String contents;

    public TextNode(String contents) {
        super(List.of());
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
