package adudecalledleo.tbsquared.text.parse.tag.style;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;
import adudecalledleo.tbsquared.text.parse.tag.color.ColorSelector;
import org.jetbrains.annotations.Nullable;

public final class StyleTag extends Tag {
    public static final String NAME = "style";

    private final @Nullable String font;
    private final @Nullable Integer size;
    private final @Nullable ColorSelector color;

    public StyleTag(Map<String, String> attributes) {
        super(NAME, attributes);

        String fontStr = attributes.get("font");
        if (fontStr == null) {
            font = null;
        } else {
            fontStr = fontStr.trim();
            if (fontStr.isEmpty()) {
                throw new IllegalArgumentException("font cannot be blank");
            }
            font = fontStr;
        }

        String sizeStr = attributes.get("size");
        if (sizeStr == null) {
            size = null;
        } else {
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
        if (colorStr == null) {
            color = null;
        } else {
            color = ColorSelector.parse(colorStr);
        }
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.pushStyle(style -> {
            if (color != null) {
                style = style.withColor(color.getColor(ctx));
            }
            if (font != null) {
                style = style.withFont(font);
            }
            if (size != null) {
                style = style.withSizeAdjust(style.sizeAdjust().orElse(0) + size);
            }
            return style;
        });
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.popStyle();
    }
}
