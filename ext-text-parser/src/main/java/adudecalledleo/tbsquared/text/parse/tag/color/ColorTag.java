package adudecalledleo.tbsquared.text.parse.tag.color;

import java.awt.*;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

public final class ColorTag extends Tag {
    public static final String NAME = "adudecalledleo/tbsquared/text/parse/tag/color";

    private final ColorSelector colorSelector;

    public ColorTag(Map<String, String> attributes) {
        super(NAME, attributes);

        var value = attributes.get("value");
        if (value == null) {
            throw new IllegalArgumentException("missing \"value\" attribute");
        }
        colorSelector = ColorSelector.parse(value);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        Color newColor = colorSelector.getColor(ctx);
        textBuilder.pushStyle(style -> style.withColor(newColor));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.popStyle();
    }
}
