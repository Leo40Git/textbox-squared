package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class StrikethroughNode extends ContainerNode {
    public static final String NAME = "s";
    public static final NodeHandler<StrikethroughNode> HANDLER = new Handler();

    public StrikethroughNode(Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
    }

    private static final class Handler implements NodeHandler<StrikethroughNode> {
        @Override
        public StrikethroughNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                       Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            return new StrikethroughNode(openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, StrikethroughNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withStrikethrough(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
