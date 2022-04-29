package adudecalledleo.tbsquared.parse.node;

import java.util.List;

import adudecalledleo.tbsquared.text.TextBuilder;

public final class Document extends ContainerNode {
    public static final String NAME = "doc";
    public static final NodeHandler<Document> HANDLER = new Handler();

    public Document(List<Node> children) {
        super(NAME, children);
    }

    public Document() {
        super(NAME);
    }

    public static final class Handler extends ImplicitNodeHandler<Document> {
        private Handler() {
            super(NAME);
        }

        @Override
        public void convert(NodeConversionContext ctx, Document node, TextBuilder tb) {
            ctx.convert(node.getChildren(), tb);
        }
    }
}
