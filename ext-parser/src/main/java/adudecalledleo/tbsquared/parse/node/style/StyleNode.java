package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.parse.node.color.ColorSelector;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public final class StyleNode extends ContainerNode {
    public static final String NAME = "style";
    public static final NodeHandler<StyleNode> HANDLER = new Handler();

    public static void register(NodeRegistry registry) {
        registry.register(NAME, HANDLER);
    }

    private final @Nullable String fontKey;
    private final @Nullable Integer size;
    private final @Nullable ColorSelector colorSelector;

    public StyleNode(@Nullable String fontKey, @Nullable Integer size, @Nullable ColorSelector colorSelector,
                     Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
        this.fontKey = fontKey;
        this.size = size;
        this.colorSelector = colorSelector;
    }

    public @Nullable String getFontKey() {
        return fontKey;
    }

    public @Nullable Integer getSize() {
        return size;
    }

    public @Nullable ColorSelector getColorSelector() {
        return colorSelector;
    }

    private static final class Handler implements NodeHandler<StyleNode> {
        @Override
        public StyleNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                               Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            @Nullable String fontKey = null;
            @Nullable Integer size = null;
            @Nullable ColorSelector color = null;

            var fontAttr = attributes.get("font");
            if (fontAttr != null) {
                String fontStr = fontAttr.value().trim();
                if (fontStr.isEmpty()) {
                    throw new IllegalArgumentException("font cannot be blank");
                }
                fontKey = fontStr;
            }

            var sizeAttr = attributes.get("size");
            if (sizeAttr != null) {
                String sizeStr = sizeAttr.value().trim();
                if (sizeStr.isEmpty()) {
                    throw new IllegalArgumentException("size cannot be blank");
                }
                char firstChar = sizeStr.charAt(0);
                if (firstChar != '-' && firstChar != '+') {
                    throw new IllegalArgumentException("size must start with + or -");
                }
                try {
                    size = Integer.parseInt(sizeStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("size must be a valid integer", e);
                }
            }

            var colorAttr = attributes.get("color");
            if (colorAttr != null) {
                color = ColorSelector.parse(colorAttr.value());
            }

            return new StyleNode(fontKey, size, color, openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, StyleNode node, TextBuilder tb) {
            tb.pushStyle(style -> {
                if (node.getColorSelector() != null) {
                    var color = node.getColorSelector().getColor(ctx.metadata());
                    if (color == null) {
                        return style.withDefaultColor();
                    } else {
                        return style.withColor(color);
                    }
                }
                if (node.getFontKey() != null) {
                    style = style.withFont(node.getFontKey());
                }
                if (node.getSize() != null) {
                    style = style.withSizeAdjust(style.sizeAdjust().orElse(0) + node.getSize());
                }
                return style;
            });
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
