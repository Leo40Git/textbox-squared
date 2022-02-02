package adudecalledleo.tbsquared.font;

import java.awt.*;
import java.util.Set;

public final class SingleFontProvider implements FontProvider {
    public static FontProvider of(Font baseFont, FontMetadata fontMetadata) {
        return new SingleFontProvider(baseFont, fontMetadata);
    }

    public static final String THE_ONLY_KEY = "the_one";
    private static final Set<String> FONT_KEYS = Set.of(THE_ONLY_KEY);

    private final StyledFontCache styledFontCache;
    private final FontMetadata fontMetadata;

    public SingleFontProvider(Font baseFont, FontMetadata fontMetadata) {
        styledFontCache = new StyledFontCache(baseFont);
        this.fontMetadata = fontMetadata;
    }

    @Override
    public Set<String> getFontKeys() {
        return FONT_KEYS;
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
    public boolean hasFontKey(String key) {
        return THE_ONLY_KEY.equals(key);
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
