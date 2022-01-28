package adudecalledleo.tbsquared.font;

import java.awt.*;

public final class AggregateFontProvider implements FontProvider {
    private final FontProvider[] providers;
    private final String defaultFontKey;
    private FontProvider defaultFontProvider;

    public AggregateFontProvider(String defaultFontKey, FontProvider... providers) {
        this.defaultFontKey = defaultFontKey;
        this.providers = providers.clone();
    }

    @Override
    public boolean hasFontKey(String key) {
        for (var provider : providers) {
            if (provider.hasFontKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Font getBaseFont(String key) {
        for (var provider : providers) {
            if (provider.hasFontKey(key)) {
                return provider.getBaseFont(key);
            }
        }
        throw FontProvider.unknownKeyException(key);
    }

    @Override
    public Font getStyledFont(String key, FontStyle style) {
        for (var provider : providers) {
            if (provider.hasFontKey(key)) {
                return provider.getStyledFont(key, style);
            }
        }
        throw FontProvider.unknownKeyException(key);
    }

    @Override
    public FontMetadata getFontMetadata(String key) {
        for (var provider : providers) {
            if (provider.hasFontKey(key)) {
                return provider.getFontMetadata(key);
            }
        }
        throw FontProvider.unknownKeyException(key);
    }

    @Override
    public String getDefaultFontKey() {
        return defaultFontKey;
    }
    
    private void lookForDefaultFontProvider() {
        if (defaultFontProvider == null) {
            for (var provider : providers) {
                if (provider.hasFontKey(defaultFontKey)) {
                    defaultFontProvider = provider;
                    break;
                }
            }
            if (defaultFontProvider == null) {
                throw new InternalError("Unknown font key as default?!");
            }
        }
    }

    @Override
    public Font getDefaultBaseFont() {
        lookForDefaultFontProvider();
        return defaultFontProvider.getBaseFont(defaultFontKey);
    }

    @Override
    public Font getDefaultStyledFont(FontStyle style) {
        lookForDefaultFontProvider();
        return defaultFontProvider.getStyledFont(defaultFontKey, style);
    }

    @Override
    public FontMetadata getDefaultFontMetadata() {
        lookForDefaultFontProvider();
        return defaultFontProvider.getFontMetadata(defaultFontKey);
    }
}
