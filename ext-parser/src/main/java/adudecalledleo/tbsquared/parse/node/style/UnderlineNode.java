package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class UnderlineNode extends ContainerNode {
    public static final String NAME = "u";
    public static final NodeHandler<UnderlineNode> HANDLER = new Handler();

    public UnderlineNode(Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
    }

    private static final class Handler implements NodeHandler<UnderlineNode> {
        @Override
        public UnderlineNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                   Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            return new UnderlineNode(openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, UnderlineNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withUnderline(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
