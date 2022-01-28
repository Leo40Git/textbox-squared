package adudecalledleo.tbsquared.text.modifier;

import java.awt.*;

import adudecalledleo.tbsquared.color.IndexedColorProvider;
import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.node.ErrorNode;
import adudecalledleo.tbsquared.text.node.ModifierNode;
import adudecalledleo.tbsquared.text.node.NodeList;
import adudecalledleo.tbsquared.util.Span;

import static adudecalledleo.tbsquared.text.modifier.ModifierParser.modLen;

public final class ColorModifierNode extends ModifierNode {
    public static final char KEY = 'c';

    private final Color color;

    public ColorModifierNode(int start, int length, Color color, Span... argSpans) {
        super(start, length, KEY, argSpans);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static final class Parser implements ModifierParser {
        public static final String ERROR_PREFIX = "Color modifier: ";

        public static final DataKey<IndexedColorProvider> INDEXED_COLOR_PROVIDER
                = new DataKey<>(IndexedColorProvider.class, "indexed_color_provider");

        @Override
        public void parse(DataTracker ctx, int start, int argsStart, String args, NodeList nodes) {
            IndexedColorProvider indexedColors = ctx.get(INDEXED_COLOR_PROVIDER).orElse(null);

            if (args == null || args.isBlank()) {
                String desc = "hex color";
                if (indexedColors != null) {
                    desc = "either color index or " + desc;
                }
                nodes.add(new ErrorNode(start, modLen(args),
                        ERROR_PREFIX + "1 argument required, " + desc));
                return;
            }

            if (args.startsWith("#")) {
                String hex = args.substring(1);
                int hexLen = hex.length();
                if (hexLen != 3 && hexLen != 6) {
                    nodes.add(new ErrorNode(argsStart, hexLen + 1,
                            ERROR_PREFIX + "Invalid hex color, should be either 6 or 3 characters long"));
                    return;
                }
                int rgb;
                try {
                    rgb = Integer.parseUnsignedInt(hex, 16);
                } catch (NumberFormatException e) {
                    nodes.add(new ErrorNode(argsStart, hexLen + 1,
                            ERROR_PREFIX + "Couldn't parse hex color value"));
                    return;
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
                nodes.add(new ColorModifierNode(start, 2 + hexLen + 3,
                        new Color(r | g << 8 | b << 16, false),
                        new Span(argsStart, hexLen + 1)));
            } else if (indexedColors != null) {
                int index;
                try {
                    index = Integer.parseUnsignedInt(args);
                } catch (NumberFormatException e) {
                    nodes.add(new ErrorNode(argsStart, args.length(),
                            ERROR_PREFIX + "Couldn't parse color index"));
                    return;
                }
                if (index >= indexedColors.getColorIndexCount()) {
                    nodes.add(new ErrorNode(argsStart, args.length(),
                            ERROR_PREFIX + "Color index is too high, max is " + (indexedColors.getColorIndexCount() - 1)));
                    return;
                }
                nodes.add(new ColorModifierNode(start, 2 + args.length() + 3,
                        indexedColors.getColorByIndex(index),
                        new Span(argsStart, args.length())));
            } else {
                nodes.add(new ErrorNode(argsStart, args.length(),
                        ERROR_PREFIX + "Color indexes are not supported! Use hex color instead"));
            }
        }
    }
}
