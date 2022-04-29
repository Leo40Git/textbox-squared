package adudecalledleo.tbsquared.parse.node.color;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class ColorNode extends ContainerNode {
    public static final String NAME = "color";
    public static final NodeHandler<ColorNode> HANDLER = new Handler();

    public static void register(NodeRegistry registry) {
        registry.register(NAME, HANDLER);
    }

    private final ColorSelector selector;

    public ColorNode(ColorSelector selector, List<Node> children) {
        super(NAME, children);
        this.selector = selector;
    }

    public ColorSelector getSelector() {
        return selector;
    }

    private static final class Handler implements NodeHandler<ColorNode> {
        @Override
        public ColorNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors, Map<String, String> attributes, String contents) {
            String colorStr = attributes.get("value");
            if (colorStr == null) {
                throw new IllegalArgumentException("Missing required attribute \"value\"");
            }
            return new ColorNode(ColorSelector.parse(colorStr), ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, ColorNode node, TextBuilder tb) {
            tb.pushStyle(style -> {
                var color = node.getSelector().getColor(ctx.metadata());
                if (color == null) {
                    return style.withDefaultColor();
                } else {
                    return style.withColor(color);
                }
            });
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
