package adudecalledleo.tbsquared.parse.node;

import java.util.Map;

import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class TextNode extends Node {
    public static final String NAME = "text";
    public static final NodeHandler<TextNode> HANDLER = new Handler();

    private final String contents;

    public TextNode(String contents) {
        super(NAME, Span.INVALID, Span.INVALID, Map.of());
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
