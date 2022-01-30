package adudecalledleo.tbsquared.text.parse.tag;

import java.awt.*;
import java.util.Map;

import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class ColorTag extends Tag {
    public static final String NAME = "color";

    private final String value;
    private Color oldColor;

    public ColorTag(Map<String, String> attributes) {
        super(NAME, attributes);

        // TODO parse value into something that can do "Color getColor(DataTracker ctx)"
        value = attributes.get("value");
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        oldColor = textBuilder.getStyle().color().orElseThrow(() -> new IllegalStateException("no color set at all?!"));
        Color newColor = parseColor(ctx, value);
        textBuilder.style(style -> style.withColor(newColor));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withColor(oldColor));
    }

    public static final DataKey<Palette> PALETTE = new DataKey<>(Palette.class, "palette");

    public static Color parseColor(DataTracker ctx, String value) {
        if (value.startsWith("#")) {
            value = value.substring(1);
            int hexLen = value.length();
            if (hexLen != 3 && hexLen != 6) {
                throw new IllegalArgumentException("unknown hex color format, should be 3 or 6 chars long");
            }
            int rgb;
            try {
                rgb = Integer.parseUnsignedInt(value, 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("failed to parse hex color", e);
            }
            int r, g, b;
            if (hexLen == 3) {
                // CSS-style
                r = rgb & 0xF;
                r += r << 4;
                g = (rgb >> 4) & 0xF;
                g += g << 4;
                b = (rgb >> 8) & 0xF;
                b += b << 4;
            } else {
                // standard
                r = rgb & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = (rgb >> 16) & 0xFF;
            }
            return new Color(r | g << 8 | b << 16, false);
        } else if (value.startsWith("palette(") && value.endsWith(")")) {
            var palette = ctx.get(PALETTE).orElse(null);
            if (palette == null) {
                throw new IllegalArgumentException("palette reference is unsupported");
            } else {
                String palIdxStr = value.substring(value.indexOf('(') + 1, value.indexOf(')')).trim();
                int palIdx;
                try {
                    palIdx = Integer.parseInt(palIdxStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("failed to parse palette index", e);
                }
                if (palIdx >= palette.getSize()) {
                    throw new IllegalArgumentException("palette index is too high");
                }
                return palette.getColor(palIdx);
            }
        }
        throw new IllegalArgumentException("couldn't parse color");
    }
}
