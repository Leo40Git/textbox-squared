package adudecalledleo.tbsquared.text.parse.tag.color;

import java.awt.*;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

public final class ColorTag extends Tag {
    public static final String NAME = "color";

    private final ColorSelector colorSelector;
    private Color oldColor;

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
        oldColor = textBuilder.getStyle().color().orElseThrow(() -> new IllegalStateException("no color set at all?!"));
        Color newColor = colorSelector.getColor(ctx);
        textBuilder.style(style -> style.withColor(newColor));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withColor(oldColor));
    }
}
