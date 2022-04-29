package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class SubscriptNode extends ContainerNode {
    public static final String NAME = "sub";
    public static final NodeHandler<SubscriptNode> HANDLER = new Handler();

    public SubscriptNode(Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
    }

    private static final class Handler implements NodeHandler<SubscriptNode> {
        @Override
        public SubscriptNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                   Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            return new SubscriptNode(openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, SubscriptNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withSuperscript(FontStyle.Superscript.SUB));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
