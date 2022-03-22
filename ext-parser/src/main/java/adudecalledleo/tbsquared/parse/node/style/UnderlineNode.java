package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class UnderlineNode extends AbstractContainerNode {
    public static final String NAME = "u";
    public static final NodeHandler<UnderlineNode> HANDLER = new Handler();

    public UnderlineNode(List<Node> children) {
        super(NAME, children);
    }

    private static final class Handler implements NodeHandler<UnderlineNode> {
        @Override
        public UnderlineNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors, Map<String, String> attributes, String contents) {
            return new UnderlineNode(ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, UnderlineNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withUnderline(true));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
