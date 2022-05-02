package adudecalledleo.tbsquared.parse.node.color;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ClassCanBeRecord")
final class ColorSelectors {
    private ColorSelectors() { }

    public static ColorSelector parse(String value) {
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
            return new ConstSelector(new Color(r | g << 8 | b << 16, false));
        } else {
            value = value.trim();
            if (NullSelector.NAME.equals(value)) {
                return NullSelector.INSTANCE;
            } else if (value.contains("(") && value.contains(")")) {
                int obIdx = value.indexOf('(');
                String argsStr = value.substring(obIdx + 1, value.indexOf(')')).trim();
                value = value.substring(0, obIdx).trim();
                /// function-like - value is function name
                if (PalIdxSelector.NAME.equals(value)) {
                    int palIdx;
                    try {
                        palIdx = Integer.parseUnsignedInt(argsStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("failed to parse palette index", e);
                    }
                    return new PalIdxSelector(palIdx);
                } else if ("rgb".equals(value)) {
                    String[] compsStr = argsStr.split(",");
                    if (compsStr.length != 3) {
                        throw new IllegalArgumentException("rgb function takes 3 arguments");
                    }
                    int r = parseRGBComponent(compsStr[0], "red");
                    int g = parseRGBComponent(compsStr[1], "green");
                    int b = parseRGBComponent(compsStr[2], "blue");
                    return new ConstSelector(new Color(r, g, b, 255));
                }
            }
        }
        throw new IllegalArgumentException("unknown color selector \"%s\"".formatted(value));
    }

    private static int parseRGBComponent(String str, String name) {
        int i;
        try {
            i = Integer.parseInt(str.trim(), 10);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("failed to parse " + name + " component", e);
        }
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException(name + " component must be between 0 and 255");
        }
        return i;
    }

    public static final class NullSelector implements ColorSelector {
        public static final String NAME = "default";
        public static final ColorSelector INSTANCE = new NullSelector();

        @Override
        public @Nullable Color getColor(DataTracker ctx) {
            return null;
        }

        @Override
        public String toString() {
            return NAME;
        }
    }

    public static final class ConstSelector implements ColorSelector {
        private final Color color;

        public ConstSelector(Color color) {
            this.color = color;
        }

        @Override
        public Color getColor(DataTracker ctx) {
            return color;
        }

        @Override
        public String toString() {
            return "#%06X".formatted(color.getRGB());
        }
    }

    public static final class PalIdxSelector implements ColorSelector {
        public static final String NAME = "palette";

        private final int palIdx;

        public PalIdxSelector(int palIdx) {
            this.palIdx = palIdx;
        }

        @Override
        public Color getColor(DataTracker ctx) {
            var pal = ctx.get(PALETTE)
                    .orElseThrow(() -> new IllegalArgumentException("palette reference is unsupported"));
            if (palIdx >= pal.getSize()) {
                throw new IllegalArgumentException("palette index is too high");
            }
            return pal.getColor(palIdx);
        }

        @Override
        public String toString() {
            return NAME + "(" + palIdx + ")";
        }
    }
}
