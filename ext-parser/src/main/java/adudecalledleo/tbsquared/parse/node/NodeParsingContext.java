package adudecalledleo.tbsquared.parse.node;

import java.util.*;

import adudecalledleo.tbsquared.parse.util.StringScanner;

public record NodeParsingContext(NodeRegistry registry, NodeSpanTracker spanTracker) {
    public List<Node> parse(String contents) {
        if (contents.isEmpty()) {
            return List.of();
        }

        final var list = new LinkedList<Node>();
        final var scanner = new StringScanner(contents);
        final var sb = new StringBuilder();
        boolean escaped = false;
        int openStart = 0, openEnd = 0;
        char c;
        while ((c = scanner.peek()) != StringScanner.EOF) {
            if (escaped) {
                escaped = false;
                switch (c) {
                    case 'u' -> {
                        scanner.next();
                        parseUnicodeEscape(spanTracker, 0, scanner, sb);
                    }
                    default -> {
                        sb.append(c);
                        scanner.next();
                    }
                }
            } else switch (c) {
                case '\\' -> {
                    escaped = true;
                    scanner.next();
                }
                case '[' -> {
                    if (!sb.isEmpty()) {
                        list.add(new TextNode(sb.toString()));
                        sb.setLength(0);
                    }

                    scanner.next();
                    String name = scanner.until(']')
                            .orElseThrow(() -> new IllegalArgumentException("malformed opening tag"))
                            .trim();

                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("empty tag decl");
                    }

                    openEnd = scanner.tell();
                    openStart = openEnd - name.length();

                    Map<String, String> attrs = Map.of();
                    int eqIndex = name.indexOf('=');
                    int spIndex = name.indexOf(' ');
                    if (spIndex > 0 && eqIndex > 0 && spIndex < eqIndex) {
                        // standard attrs
                        String attrString = name.substring(spIndex + 1);
                        name = name.substring(0, spIndex);
                        spanTracker.markNodeDeclOpening(name, openStart, openEnd);
                        attrs = parseAttributes(spanTracker, scanner.tell(), name, sb, attrString);
                    } else if (eqIndex >= 0) {
                        // compact [<tag>=<value>] (equal to [<tag> value="<value>"])
                        String value = name.substring(eqIndex + 1);
                        name = name.substring(0, eqIndex);
                        spanTracker.markNodeDeclOpening(name, openStart, openEnd);
                        attrs = Map.of("value", value);
                        spanTracker.markNodeDeclAttribute(name, "value", -1, -1, value,
                                eqIndex + 1, eqIndex + 1 + value.length());
                    } else {
                        // no attrs
                        spanTracker.markNodeDeclOpening(name, openStart, openEnd);
                    }

                    var handler = registry.getHandler(name);
                    if (handler == null) {
                        throw new IllegalArgumentException("unknown tag \"" + name + "\"");
                    }

                    final String nameF = name;
                    list.add(handler.parse(this, attrs,
                            scanner.until("[/%s]".formatted(name))
                                    .orElseThrow(() ->
                                            new IllegalArgumentException("missing closing tag for \"" + nameF + "\""))));

                    spanTracker.markNodeDeclClosing(name, scanner.tell() - name.length(), scanner.tell());
                }
                default -> {
                    sb.append(c);
                    scanner.next();
                }
            }
        }

        if (!sb.isEmpty()) {
            list.add(new TextNode(sb.toString()));
        }

        return list;
    }

    public static Map<String, String> parseAttributes(NodeSpanTracker spanTracker, int offset, String node, StringBuilder sb, String attrString) {
        sb.setLength(0);

        // step 1 - split string into substrings using spaces as a delimiter while respecting quotes
        record Entry(int start, String contents) { }

        List<Entry> entries = new ArrayList<>();
        final var scanner = new StringScanner(attrString);
        boolean escaped = false;
        int start = 0;
        char quoteChar = 0;
        char c;
        while ((c = scanner.peek()) != StringScanner.EOF) {
            if (escaped) {
                escaped = false;
                sb.append(c);
                scanner.next();
                spanTracker.markEscaped(offset + scanner.tell() - 2, offset + scanner.tell());
            } else {
                switch (c) {
                    case '\\' -> {
                        escaped = true;
                        sb.append(c);
                        scanner.next();
                    }
                    case '"', '\'' -> {
                        if (quoteChar == 0) {
                            quoteChar = c;
                        } else if (quoteChar == c) {
                            quoteChar = 0;
                        } else {
                            sb.append(c);
                        }
                        scanner.next();
                    }
                    case ' ' -> {
                        if (quoteChar == 0) {
                            if (!sb.isEmpty()) {
                                entries.add(new Entry(start, sb.toString()));
                                sb.setLength(0);
                                start = scanner.tell() + 1;
                            }
                        } else {
                            sb.append(c);
                        }
                        scanner.next();
                    }
                    default -> {
                        sb.append(c);
                        scanner.next();
                    }
                }
            }
        }

        if (escaped) {
            throw new IllegalArgumentException("escaping end of text?");
        }

        if (quoteChar != 0) {
            throw new IllegalArgumentException("unclosed quote " + quoteChar);
        }

        if (!sb.isEmpty()) {
            entries.add(new Entry(start, sb.toString()));
        }

        // step 2 - parse substrings into key-value pairs
        Map<String, String> attrs = new LinkedHashMap<>();
        String key, value;
        for (var entry : entries) {
            String contents = entry.contents();
            int eqIndex = contents.indexOf('=');
            if (eqIndex < 0) {
                key = toAttributeKey(contents);
                attrs.put(key, "");
                spanTracker.markNodeDeclAttribute(node, key, offset + entry.start(), offset + entry.start() + key.length(), "",
                        -1, -1);
                continue;
            }
            key = toAttributeKey(contents.substring(0, eqIndex));
            value = contents.substring(eqIndex + 1).trim();
            if ((value.startsWith("\"") && value.endsWith("\""))
             || (value.startsWith("'") && value.endsWith("'"))) {
                value = value.substring(1, value.length() - 1);
            }

            String parsedValue = parseEscapes(spanTracker, offset + entry.start() + eqIndex + 1, value, sb);
            attrs.put(key, parsedValue);
            spanTracker.markNodeDeclAttribute(node, key, offset + entry.start(), offset + entry.start() + key.length(), parsedValue,
                    offset + entry.start() + eqIndex + 1, offset + entry.start() + eqIndex + 1 + value.length());
        }

        sb.setLength(0);
        return attrs;
    }

    private static String toAttributeKey(String str) {
        String currentKey = str.trim().toLowerCase(Locale.ROOT);
        if (currentKey.isEmpty()) {
            throw new IllegalArgumentException("empty attribute key");
        }
        return currentKey;
    }

    public static String parseEscapes(NodeSpanTracker spanTracker, int offset, String string, StringBuilder sb) {
        sb.setLength(0);
        var scanner = new StringScanner(string);
        boolean escaped = false;
        char c;
        while ((c = scanner.peek()) != StringScanner.EOF) {
            if (escaped) {
                escaped = false;
                switch (c) {
                    case 'u' -> {
                        scanner.next();
                        parseUnicodeEscape(spanTracker, offset, scanner, sb);
                    }
                    default -> {
                        sb.append(c);
                        scanner.next();
                    }
                }
            } else if (c == '\\') {
                escaped = true;
                scanner.next();
            } else {
                sb.append(c);
                scanner.next();
            }
        }
        return sb.toString();
    }

    private static void parseUnicodeEscape(NodeSpanTracker spanTracker, int offset, StringScanner scanner, StringBuilder sb) {
        String charIdStr = scanner.read(4).orElseThrow(() -> new IllegalArgumentException("unicode escape has <4 digits"));
        int charId;
        try {
            charId = Integer.parseUnsignedInt(charIdStr, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("failed to parse unicode escape", e);
        }
        sb.append((char) charId);
        spanTracker.markEscaped(offset + scanner.tell() - 6, offset + scanner.tell());
    }
}
