package adudecalledleo.tbsquared.parse.node;

import adudecalledleo.tbsquared.text.TextBuilder;

public final class TextNode extends AbstractNode {
    public static final String NAME = "text";
    public static final Handler HANDLER = new Handler();

    private final String contents;

    public TextNode(String contents) {
        super(NAME);
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public static final class Handler extends ImplicitNodeHandler<TextNode> {
        private Handler() {
            super(NAME);
        }

        @Override
        public void convert(NodeConversionContext ctx, TextNode node, TextBuilder tb) {
            tb.append(node.getContents());
        }
    }
}
