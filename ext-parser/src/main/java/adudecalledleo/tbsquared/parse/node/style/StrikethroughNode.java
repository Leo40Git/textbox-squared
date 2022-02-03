package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class StrikethroughNode extends AbstractContainerNode {
    public static final String NAME = "s";
    public static final NodeHandler<StrikethroughNode> HANDLER = new Handler();

    public StrikethroughNode(List<Node> children) {
        super(NAME, children);
    }

    private static final class Handler implements NodeHandler<StrikethroughNode> {
        @Override
        public StrikethroughNode parse(NodeParsingContext ctx, Map<String, String> attributes, String contents) {
            return new StrikethroughNode(ctx.parse(contents));
        }

        @Override
        public void convert(NodeConversionContext ctx, StrikethroughNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withStrikethrough(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
