package adudecalledleo.tbsquared.parse.node;

import java.util.*;

import adudecalledleo.tbsquared.parse.util.StringScanner;

public record NodeParsingContext(NodeRegistry registry) {
    public List<Node> parse(String contents) {
        if (contents.isEmpty()) {
            return List.of();
        }

        final var list = new LinkedList<Node>();
        final var scanner = new StringScanner(contents);
        final var sb = new StringBuilder();
        boolean escaped = false;
        char c;
        while ((c = scanner.peek()) != StringScanner.EOF) {
            if (escaped) {
                escaped = false;
                switch (c) {
                    case 'u' -> parseUnicodeEscape(scanner, sb);
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

                    var handler = registry.getHandler(name);
                    if (handler == null) {
                        throw new IllegalArgumentException("unknown tag \"" + name + "\"");
                    }

                    Map<String, String> attrs = Map.of();
                    int eqIndex = name.indexOf('=');
                    int spIndex = name.indexOf(' ');
                    if (spIndex > 0 && eqIndex > 0 && spIndex < eqIndex) {
                        // standard attrs
                        String attrString = name.substring(spIndex + 1);
                        name = name.substring(0, spIndex);
                        attrs = parseAttributes(sb, attrString);
                    } else if (eqIndex >= 0) {
                        // compact [<tag>=<value>] (equal to [<tag> value="<value>"])
                        String value = name.substring(eqIndex + 1);
                        name = name.substring(0, eqIndex);
                        attrs = Map.of("value", value);
                    } // else, no attrs

                    final String nameF = name;
                    list.add(handler.parse(this, attrs,
                            scanner.until("[/%s]".formatted(name))
                                    .orElseThrow(() ->
                                            new IllegalArgumentException("missing closing tag for \"" + nameF + "\""))));
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

    public static Map<String, String> parseAttributes(StringBuilder sb, String attrString) {
        sb.setLength(0);

        Map<String, String> attrs;
        attrs = new LinkedHashMap<>();

        final var scanner = new StringScanner(attrString);
        boolean escaped = false;
        char quoteChar = 0;
        String currentKey = null;
        char c;
        while ((c = scanner.peek()) != StringScanner.EOF) {
            if (escaped) {
                escaped = false;
                switch (c) {
                    case 'u' -> parseUnicodeEscape(scanner, sb);
                    default -> {
                        sb.append(c);
                        scanner.next();
                    }
                }
            } else {
                switch (c) {
                    case '\\' -> {
                        escaped = true;
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
                    case '=' -> {
                        if (quoteChar == 0) {
                            if (sb.isEmpty()) {
                                throw new IllegalArgumentException("unexpected =");
                            }
                            currentKey = getAttributeKey(sb);
                            sb.setLength(0);
                        }
                        scanner.next();
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
                        scanner.next();
                    }
                    default -> {
                        sb.append(c);
                        scanner.next();
                    }
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

    private static void parseUnicodeEscape(StringScanner scanner, StringBuilder sb) {
        String charIdStr = scanner.read(4).orElseThrow(() -> new IllegalArgumentException("unicode escape has <4 digits"));
        int charId;
        try {
            charId = Integer.parseUnsignedInt(charIdStr, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("failed to parse unicode escape", e);
        }
        sb.append((char) charId);
    }
}
