package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class ItalicNode extends ContainerNode {
    public static final String NAME = "i";
    public static final NodeHandler<ItalicNode> HANDLER = new Handler();

    public ItalicNode(List<Node> children) {
        super(NAME, children);
    }

    private static final class Handler implements NodeHandler<ItalicNode> {
        @Override
        public ItalicNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors, Map<String, String> attributes, String contents) {
            return new ItalicNode(ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, ItalicNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withItalic(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
