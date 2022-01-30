package adudecalledleo.tbsquared.text.parse;

import java.awt.*;
import java.util.List;
import java.util.*;

import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;
import adudecalledleo.tbsquared.text.parse.tag.TagRegistry;

public final class TextParser {
    private TextParser() { }

    public static final DataKey<Color> DEFAULT_COLOR = new DataKey<>(Color.class, "default_color");

    public static Text parse(DataTracker ctx, String text) {
        text = TextSanitizer.apply(text);

        var sb = new StringBuilder();
        var tb = new TextBuilder();
        tb.style(style -> style.withColor(ctx.get(DEFAULT_COLOR).orElse(Color.WHITE)));
        List<Tag> tagStack = new ArrayList<>();
        boolean escaped = false;
        final char[] chars = text.toCharArray();
        int pos;
        for (pos = 0; pos < chars.length; pos++) {
            char c = chars[pos];
            if (escaped) {
                escaped = false;
                switch (c) {
                    case 'u' -> {
                        // TODO handle unicode escape
                    }
                    default -> tb.append(c);
                }
            } else {
                switch (c) {
                case '\\' -> escaped = true;
                    case '[' -> {
                        pos++;
                        boolean closingTag = false;
                        if (pos < chars.length) {
                            closingTag = chars[pos] == '/';
                            if (closingTag) {
                                pos++;
                            }
                        }
                        boolean foundEnd = false;
                        while (pos < chars.length) {
                            if (chars[pos] == ']') {
                                foundEnd = true;
                                break;
                            }
                            sb.append(chars[pos]);
                            pos++;
                        }
                        if (!foundEnd) {
                            throw new IllegalArgumentException("unclosed tag decl");
                        }
                        String tagName = sb.toString().trim().toLowerCase(Locale.ROOT);
                        sb.setLength(0);

                        Map<String, String> attrs = Map.of();
                        if (!closingTag) {
                            int eqIndex = tagName.indexOf('=');
                            int spIndex = tagName.indexOf(' ');
                            if (spIndex > 0 && eqIndex > 0 && spIndex < eqIndex) {
                                // standard attrs
                                String attrString = tagName.substring(spIndex + 1);
                                tagName = tagName.substring(0, spIndex);
                                attrs = parseTagAttributes(sb, attrString);
                            } else if (eqIndex >= 0) {
                                // compact tag=value format
                                String value = tagName.substring(eqIndex + 1);
                                tagName = tagName.substring(0, eqIndex);
                                attrs = Map.of("value", value);
                            } // else, no attrs
                        }

                        if (tagName.isEmpty()) {
                            throw new IllegalArgumentException("empty tag decl");
                        }

                        var tagFac = TagRegistry.get(tagName);
                        if (tagFac == null) {
                            throw new IllegalArgumentException("unknown tag " + tagName);
                        }

                        if (closingTag) {
                            if (tagStack.isEmpty()) {
                                throw new IllegalArgumentException("closing nonexistent tag " + tagName);
                            } else {
                                var it = tagStack.listIterator();
                                while (it.hasNext()) {
                                    var tag = it.next();
                                    if (tagName.equals(tag.getName())) {
                                        tag.onClose(ctx, tb);
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                        } else {
                            var tag = tagFac.createTag(attrs);
                            tag.onOpen(ctx, tb);
                            tagStack.add(0, tag);
                        }
                    }
                    default -> tb.append(c);
                }
            }
        }
        if (!tagStack.isEmpty()) {
            var tag = tagStack.remove(0);
            throw new IllegalArgumentException("unclosed tag " + tag.getName());
        }
        return tb.build();
    }

    private static Map<String, String> parseTagAttributes(StringBuilder sb, String attrString) {
        Map<String, String> attrs;
        attrs = new LinkedHashMap<>();

        final char[] chars = attrString.toCharArray();
        int pos;
        boolean escaped = false;
        char quoteChar = 0;
        String currentKey = null;
        for (pos = 0; pos < chars.length; pos++) {
            char c = chars[pos];
            if (escaped) {
                escaped = false;
                sb.append(c);
            } else {
                switch (c) {
                    case '\\' -> escaped = true;
                    case '"', '\'' -> {
                        if (quoteChar == 0) {
                            quoteChar = c;
                        } else if (quoteChar == c) {
                            quoteChar = 0;
                        } else {
                            sb.append(c);
                        }
                    }
                    case '=' -> {
                        currentKey = getAttributeKey(sb);
                        sb.setLength(0);
                    }
                    case ' ' -> {
                        if (quoteChar == 0) {
                            if (!sb.isEmpty()) {
                                String value = "";
                                if (currentKey == null) {
                                    currentKey = getAttributeKey(sb);
                                } else {
                                    value = sb.toString();
                                }
                                sb.setLength(0);
                                attrs.put(currentKey, value);
                            }
                        } else {
                            sb.append(c);
                        }
                    }
                    default -> sb.append(c);
                }
            }
        }

        if (quoteChar != 0) {
            throw new IllegalArgumentException("unclosed quote " + quoteChar);
        }

        if (!sb.isEmpty()) {
            String value = "";
            if (currentKey == null) {
                currentKey = getAttributeKey(sb);
            } else {
                value = sb.toString();
            }
            sb.setLength(0);
            attrs.put(currentKey, value);
        }

        sb.setLength(0);
        return attrs;
    }

    private static String getAttributeKey(StringBuilder sb) {
        String currentKey = sb.toString().trim().toLowerCase(Locale.ROOT);
        if (currentKey.isEmpty()) {
            throw new IllegalArgumentException("empty attribute key");
        }
        return currentKey;
    }
}
