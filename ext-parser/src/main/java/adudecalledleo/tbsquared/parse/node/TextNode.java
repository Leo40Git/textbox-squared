package adudecalledleo.tbsquared.parse.node;

public final class TextNode extends AbstractNode {
    public static final String NAME = "text";

    private final String contents;

    public TextNode(String contents) {
        super(NAME);
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
