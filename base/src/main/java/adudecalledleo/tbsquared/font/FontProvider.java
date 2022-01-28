package adudecalledleo.tbsquared.font;

import java.awt.*;

public interface FontProvider {
    boolean hasFontKey(String key);
    Font getBaseFont(String key);
    Font getStyledFont(String key, FontStyle style);
    FontMetadata getFontMetadata(String key);
    String getDefaultFontKey();

    default Font getDefaultBaseFont() {
        return getBaseFont(getDefaultFontKey());
    }

    default Font getDefaultStyledFont(FontStyle style) {
        return getStyledFont(getDefaultFontKey(), style);
    }

    default FontMetadata getDefaultFontMetadata() {
        return getFontMetadata(getDefaultFontKey());
    }

    static IllegalArgumentException unknownKeyException(String key) {
        throw new IllegalArgumentException("Unknown font key \"%s\"".formatted(key));
    }
}
