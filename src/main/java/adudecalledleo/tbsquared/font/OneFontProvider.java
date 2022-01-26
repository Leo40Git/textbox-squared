package adudecalledleo.tbsquared.font;

import java.awt.*;

public final class OneFontProvider implements FontProvider {
    public static FontProvider of(Font baseFont, FontMetadata fontMetadata) {
        return new OneFontProvider(baseFont, fontMetadata);
    }

    public static final String THE_ONLY_KEY = "the_one";

    private final StyledFontCache styledFontCache;
    private final FontMetadata fontMetadata;

    private OneFontProvider(Font baseFont, FontMetadata fontMetadata) {
        styledFontCache = new StyledFontCache(baseFont);
        this.fontMetadata = fontMetadata;
    }

    @Override
    public boolean hasFontKey(String key) {
        return THE_ONLY_KEY.equals(key);
    }

    @Override
    public Font getBaseFont(String key) {
        if (!THE_ONLY_KEY.equals(key)) {
            throw FontProvider.unknownKeyException(key);
        }
        return styledFontCache.getBaseFont();
    }

    @Override
    public Font getStyledFont(String key, FontStyle style) {
        if (!THE_ONLY_KEY.equals(key)) {
            throw FontProvider.unknownKeyException(key);
        }
        return styledFontCache.getStyledFont(style);
    }

    @Override
    public FontMetadata getFontMetadata(String key) {
        if (!THE_ONLY_KEY.equals(key)) {
            throw FontProvider.unknownKeyException(key);
        }
        return fontMetadata;
    }

    @Override
    public String getDefaultFontKey() {
        return THE_ONLY_KEY;
    }

    @Override
    public Font getDefaultBaseFont() {
        return styledFontCache.getBaseFont();
    }

    @Override
    public Font getDefaultStyledFont(FontStyle style) {
        return styledFontCache.getStyledFont(style);
    }

    @Override
    public FontMetadata getDefaultFontMetadata() {
        return fontMetadata;
    }
}
