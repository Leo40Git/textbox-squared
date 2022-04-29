package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class SuperscriptNode extends ContainerNode {
    public static final String NAME = "sup";
    public static final NodeHandler<SuperscriptNode> HANDLER = new Handler();

    public SuperscriptNode(Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
    }

    private static final class Handler implements NodeHandler<SuperscriptNode> {
        @Override
        public SuperscriptNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                     Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            return new SuperscriptNode(openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, SuperscriptNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withSuperscript(FontStyle.Superscript.SUPER));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
