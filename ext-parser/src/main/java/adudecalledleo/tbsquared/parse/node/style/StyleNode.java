package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.parse.node.color.ColorSelector;
import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public final class StyleNode extends AbstractContainerNode {
    public static final String NAME = "style";
    public static final NodeHandler<StyleNode> HANDLER = new Handler();

    public static void register(NodeRegistry registry) {
        registry.register(NAME, HANDLER);
    }

    private final @Nullable String font;
    private final @Nullable Integer size;
    private final @Nullable ColorSelector colorSelector;

    public StyleNode(@Nullable String font, @Nullable Integer size, @Nullable ColorSelector colorSelector, List<Node> children) {
        super(NAME, children);
        this.font = font;
        this.size = size;
        this.colorSelector = colorSelector;
    }

    public @Nullable String getFont() {
        return font;
    }

    public @Nullable Integer getSize() {
        return size;
    }

    public @Nullable ColorSelector getColorSelector() {
        return colorSelector;
    }

    private static final class Handler implements NodeHandler<StyleNode> {
        @Override
        public StyleNode parse(NodeParsingContext ctx, Map<String, String> attributes, String contents) {
            @Nullable String font = null;
            @Nullable Integer size = null;
            @Nullable ColorSelector color = null;

            String fontStr = attributes.get("font");
            if (fontStr != null) {
                fontStr = fontStr.trim();
                if (fontStr.isEmpty()) {
                    throw new IllegalArgumentException("font cannot be blank");
                }
                font = fontStr;
            }

            String sizeStr = attributes.get("size");
            if (sizeStr != null) {
                sizeStr = sizeStr.trim();
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

            String colorStr = attributes.get("color");
            if (colorStr != null) {
                color = ColorSelector.parse(colorStr);
            }

            return new StyleNode(font, size, color, ctx.parse(contents));
        }

        @Override
        public void convert(NodeConversionContext ctx, StyleNode node, TextBuilder tb) {
            tb.pushStyle(style -> {
                if (node.getColorSelector() != null) {
                    style = style.withColor(node.getColorSelector().getColor(ctx.metadata()));
                }
                if (node.getFont() != null) {
                    style = style.withFont(node.getFont());
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
