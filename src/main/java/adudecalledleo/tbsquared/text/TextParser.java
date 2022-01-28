package adudecalledleo.tbsquared.text;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.modifier.ModifierRegistry;
import adudecalledleo.tbsquared.text.node.ErrorNode;
import adudecalledleo.tbsquared.text.node.LineBreakNode;
import adudecalledleo.tbsquared.text.node.NodeList;
import adudecalledleo.tbsquared.text.node.TextNode;

public final class TextParser {
    private final StringBuilder sb;
    private int textStart, textLength;
    private NodeList nodes;

    public TextParser() {
        sb = new StringBuilder();
    }

    public NodeList parse(DataTracker ctx, String text) {
        text = TextSanitizer.apply(text);

        char[] chars = text.toCharArray();
        sb.setLength(0);
        nodes = new NodeList();
        textStart = 0;
        textLength = 0;

        boolean backslash = false;
        int pos;
        for (pos = 0; pos < chars.length; pos++) {
            char c = chars[pos];
            if (backslash) {
                backslash = false;
                flushTextNode();
                if (c == '\\') {
                    nodes.add(new TextNode.Escaped(pos - 1, 2, "\\", "\\\\"));
                    textStart += 2;
                } else {
                    int modStartPos = pos++ - 1;
                    var parser = ModifierRegistry.get(c);
                    if (pos < chars.length && chars[pos] == '[') {
                        final int start = pos++;
                        boolean end = false;
                        while (pos < chars.length) {
                            char c2 = chars[pos];
                            if (c2 == ']') {
                                end = true;
                                break;
                            } else {
                                if (parser != null) {
                                    sb.append(c2);
                                }
                                pos++;
                            }
                        }
                        if (!end) {
                            nodes.add(new ErrorNode(start, pos - start, "Unclosed argument brackets []"));
                            continue;
                        }
                        if (parser == null) {
                            nodes.add(new ErrorNode(modStartPos, pos - modStartPos + 1, "Unknown modifier key '" + c + "'"));
                        } else {
                            parser.parse(ctx, modStartPos, start + 1, sb.toString(), nodes);
                            sb.setLength(0);
                        }
                        textStart = pos + 1;
                    } else {
                        if (parser == null) {
                            nodes.add(new ErrorNode(modStartPos, 2, "Unknown modifier key '" + c + "'"));
                        } else {
                            parser.parse(ctx, modStartPos, -1, null, nodes);
                        }
                        textStart = pos;
                        pos--;
                    }
                }
            } else {
                if (c == '\\') {
                    backslash = true;
                } else if (c == '\n') {
                    flushTextNode();
                    nodes.add(new LineBreakNode(pos));
                    textStart = pos + 1;
                } else {
                    sb.append(c);
                    textLength++;
                }
            }
        }

        flushTextNode();
        if (backslash) {
            nodes.add(new ErrorNode(chars.length - 1, 1, "Backslash at end of text"));
        }
        nodes.optimizeTextNodes();
        nodes.checkForAdditionalErrors();
        return nodes;
    }

    private void flushTextNode() {
        if (textLength > 0) {
            nodes.add(new TextNode(textStart, textLength, sb.toString()));
            sb.setLength(0);
            textStart += textLength;
            textLength = 0;
        }
    }
}
