package adudecalledleo.tbsquared.text.parse.tag.color;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;

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
        } else if (value.startsWith("rgb(") && value.endsWith(")")) {
            String argsStr = value.substring(value.indexOf('(') + 1, value.indexOf(')'));
            // TODO impl rgb(r, g, b)
        } else if (value.startsWith("palette(") && value.endsWith(")")) {
            String palIdxStr = value.substring(value.indexOf('(') + 1, value.indexOf(')')).trim();
            int palIdx;
            try {
                palIdx = Integer.parseInt(palIdxStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("failed to parse palette index", e);
            }
            return new PalIdxSelector(palIdx);
        }
        throw new IllegalArgumentException("couldn't parse color selector \"%s\"".formatted(value));
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
        private final int palIdx;

        public PalIdxSelector(int palIdx) {
            this.palIdx = palIdx;
        }

        @Override
        public Color getColor(DataTracker ctx) {
            var pal = ctx.get(PALETTE).orElseThrow(() -> new IllegalArgumentException("palette reference is unsupported"));
            if (palIdx >= pal.getSize()) {
                throw new IllegalArgumentException("palette index is too high");
            }
            return pal.getColor(palIdx);
        }

        @Override
        public String toString() {
            return "palette(" + palIdx + ")";
        }
    }
}
