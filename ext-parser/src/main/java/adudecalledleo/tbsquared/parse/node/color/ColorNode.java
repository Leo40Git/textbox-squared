package adudecalledleo.tbsquared.parse.node.color;

import java.awt.*;
import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public final class ColorNode extends ContainerNode {
    public static final String NAME = "color";
    public static final NodeHandler<ColorNode> HANDLER = new Handler();

    public static void register(NodeRegistry registry) {
        registry.register(NAME, HANDLER);
    }

    private final @Nullable Color color;

    public ColorNode(@Nullable Color color, Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
        this.color = color;
    }

    public @Nullable Color getColor() {
        return color;
    }

    private static final class Handler implements NodeHandler<ColorNode> {
        @Override
        public @Nullable ColorNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                         Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            var colorAttr = attributes.get("value");
            if (colorAttr == null) {
                errors.add(new DOMParser.Error(openingSpan.start(), openingSpan.length(), "Missing required attribute \"value\""));
                return null;
            }

            if (isAttributeBlank(colorAttr, errors)) {
                return null;
            }

            Color color;
            try {
                color = ColorParser.parseColor(ctx.metadata(), colorAttr.value().trim());
            } catch (IllegalArgumentException e) {
                errors.add(new DOMParser.Error(colorAttr.valueSpan().start(), colorAttr.valueSpan().length(), e.getMessage()));
                return null;
            }

            return new ColorNode(color, openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, ColorNode node, TextBuilder tb) {
            tb.pushStyle(style -> {
                if (node.getColor() == null) {
                    return style.withDefaultColor();
                } else {
                    return style.withColor(node.getColor());
                }
            });
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
