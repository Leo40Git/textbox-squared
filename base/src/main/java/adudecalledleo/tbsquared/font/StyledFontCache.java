package adudecalledleo.tbsquared.font;

import java.awt.*;
import java.awt.font.*;
import java.util.HashMap;
import java.util.Map;

public final class StyledFontCache {
    private static final ThreadLocal<Map<TextAttribute, Object>> TL_ATTR_MAP =
            ThreadLocal.withInitial(() -> new HashMap<>(6));

    private final Font baseFont;
    private final Map<FontStyle, Font> styledFonts;

    public StyledFontCache(Font baseFont) {
        this.baseFont = baseFont;
        styledFonts = new HashMap<>();
        styledFonts.put(FontStyle.DEFAULT, baseFont);
    }

    public Font getBaseFont() {
        return baseFont;
    }

    public Font getStyledFont(FontStyle style) {
        return styledFonts.computeIfAbsent(style, key -> {
            Map<TextAttribute, Object> map = TL_ATTR_MAP.get();
            map.put(TextAttribute.WEIGHT, key.bold() ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
            map.put(TextAttribute.POSTURE, key.italic() ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
            map.put(TextAttribute.UNDERLINE, key.underline() ? TextAttribute.UNDERLINE_ON : -1);
            map.put(TextAttribute.STRIKETHROUGH, key.strikethrough() ? TextAttribute.STRIKETHROUGH_ON : false);
            map.put(TextAttribute.SUPERSCRIPT, switch (key.superscript()) {
                case MID -> 0;
                case SUPER -> TextAttribute.SUPERSCRIPT_SUPER;
                case SUB -> TextAttribute.SUPERSCRIPT_SUB;
            });
            map.put(TextAttribute.SIZE, baseFont.getSize() + key.sizeAdjust());
            return baseFont.deriveFont(map);
        });
    }
}
