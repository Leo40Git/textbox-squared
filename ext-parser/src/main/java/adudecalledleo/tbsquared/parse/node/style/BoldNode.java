package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class BoldNode extends ContainerNode {
    public static final String NAME = "b";
    public static final NodeHandler<BoldNode> HANDLER = new Handler();

    public BoldNode(List<Node> children) {
        super(NAME, children);
    }

    private static final class Handler implements NodeHandler<BoldNode> {
        @Override
        public BoldNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors, Map<String, String> attributes, String contents) {
            return new BoldNode(ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, BoldNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withBold(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
