package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class SuperscriptNode extends AbstractContainerNode {
    public static final String NAME = "sup";
    public static final NodeHandler<SuperscriptNode> HANDLER = new Handler();

    public SuperscriptNode(List<Node> children) {
        super(NAME, children);
    }

    private static final class Handler implements NodeHandler<SuperscriptNode> {
        @Override
        public SuperscriptNode parse(NodeParsingContext ctx, Map<String, String> attributes, String contents) {
            return new SuperscriptNode(ctx.parse(contents));
        }

        @Override
        public void convert(NodeConversionContext ctx, SuperscriptNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withSuperscript(FontStyle.Superscript.SUPER));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
