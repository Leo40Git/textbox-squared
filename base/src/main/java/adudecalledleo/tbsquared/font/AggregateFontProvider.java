package adudecalledleo.tbsquared.font;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class AggregateFontProvider implements FontProvider {
    private final Map<String, FontProvider> keyToProviderMap;
    private final String defaultFontKey;
    private final FontProvider defaultFontProvider;

    public AggregateFontProvider(String defaultFontKey, FontProvider... providers) {
        this.defaultFontKey = defaultFontKey;
        Map<String, FontProvider> map = new HashMap<>();
        for (var provider : providers) {
            for (var fontKey : provider.getFontKeys()) {
                var provider2 = map.put(fontKey, provider);
                if (provider2 != null) {
                    throw new IllegalArgumentException("Both %s and %s contain font key \"%s\"!".formatted(provider, provider2, fontKey));
                }
            }
        }
        this.defaultFontProvider = map.get(defaultFontKey);
        if (this.defaultFontProvider == null) {
            throw new IllegalArgumentException("Default font \"%s\" is missing!".formatted(defaultFontKey));
        }
        this.keyToProviderMap = Map.copyOf(map);
    }

    @Override
    public Set<String> getFontKeys() {
        return keyToProviderMap.keySet();
    }

    @Override
    public boolean hasFontKey(String key) {
        return keyToProviderMap.containsKey(key);
    }

    private FontProvider getProviderForKey(String key) {
        var provider = keyToProviderMap.get(key);
        if (provider == null) {
            throw FontProvider.unknownKeyException(key);
        }
        return provider;
    }

    @Override
    public Font getBaseFont(String key) {
        FontProvider provider = getProviderForKey(key);
        return provider.getBaseFont(key);
    }

    @Override
    public Font getStyledFont(String key, FontStyle style) {
        FontProvider provider = getProviderForKey(key);
        return provider.getStyledFont(key, style);
    }

    @Override
    public FontMetadata getFontMetadata(String key) {
        FontProvider provider = getProviderForKey(key);
        return provider.getFontMetadata(key);
    }

    @Override
    public String getDefaultFontKey() {
        return defaultFontKey;
    }

    @Override
    public Font getDefaultBaseFont() {
        return defaultFontProvider.getBaseFont(defaultFontKey);
    }

    @Override
    public Font getDefaultStyledFont(FontStyle style) {
        return defaultFontProvider.getStyledFont(defaultFontKey, style);
    }

    @Override
    public FontMetadata getDefaultFontMetadata() {
        return defaultFontProvider.getFontMetadata(defaultFontKey);
    }
}
