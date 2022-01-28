package adudecalledleo.tbsquared.font;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DefaultFontProvider implements FontProvider {
    public record FontRecord(StyledFontCache styledCache, FontMetadata metadata) { }
    
    public static final class Builder {
        private final Map<String, FontRecord> records;
        private String defaultFontKey;

        private Builder() {
            records = new HashMap<>();
        }

        public Builder addFont(String key, Font baseFont, FontMetadata metadata) {
            records.put(key, new FontRecord(new StyledFontCache(baseFont), metadata));
            if (defaultFontKey == null) {
                defaultFontKey = key;
            }
            return this;
        }

        public Builder defaultFontKey(String key) {
            defaultFontKey = key;
            return this;
        }

        public FontProvider build() {
            return new DefaultFontProvider(records, defaultFontKey);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    protected final Map<String, FontRecord> records;
    protected final String defaultFontKey;

    public DefaultFontProvider(Map<String, FontRecord> records, String defaultFontKey) {
        this.records = Map.copyOf(records);
        this.defaultFontKey = defaultFontKey;
    }

    @Override
    public boolean hasFontKey(String key) {
        return records.containsKey(key);
    }

    protected FontRecord getFontRecord(String key) {
        var record = records.get(key);
        if (record == null) {
            throw FontProvider.unknownKeyException(key);
        }
        return record;
    }

    @Override
    public Font getBaseFont(String key) {
        return getFontRecord(key).styledCache().getBaseFont();
    }

    @Override
    public Font getStyledFont(String key, FontStyle style) {
        return getFontRecord(key).styledCache().getStyledFont(style);
    }

    @Override
    public FontMetadata getFontMetadata(String key) {
        return getFontRecord(key).metadata();
    }

    @Override
    public String getDefaultFontKey() {
        return defaultFontKey;
    }
}
