package adudecalledleo.tbsquared.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public final class TextBuilder {
    private static final class Node {
        private final TextStyle style;
        private final String contents;
        private final List<Node> children;

        public Node(TextStyle style, String contents) {
            this.style = style;
            this.contents = contents;
            this.children = new ArrayList<>();
        }

        public Text toText() {
            return new Text(style, contents, children.stream().map(Node::toText).toList());
        }
    }

    private final StringBuilder deferredContents;
    private Node root;
    private TextStyle deferredStyle;

    public TextBuilder() {
        this.deferredContents = new StringBuilder();
        deferredStyle = TextStyle.EMPTY;
    }

    private void commitDeferred() {
        if (!deferredContents.isEmpty()) {
            if (root == null) {
                root = new Node(deferredStyle, deferredContents.toString());
            } else {
                root.children.add(new Node(deferredStyle, deferredContents.toString()));
            }
            deferredContents.setLength(0);
        }
    }

    public TextBuilder style(TextStyle style) {
        if (!this.deferredStyle.equals(style)) {
            commitDeferred();
            this.deferredStyle = style;
        }
        return this;
    }

    public TextBuilder style(UnaryOperator<TextStyle> styleModifier) {
        return style(styleModifier.apply(deferredStyle));
    }

    public TextBuilder append(String contents) {
        deferredContents.append(contents);
        return this;
    }

    public TextBuilder append(char c) {
        deferredContents.append(c);
        return this;
    }

    public TextBuilder newLine() {
        return append('\n');
    }

    public Text build() {
        commitDeferred();
        if (root == null) {
            return Text.EMPTY;
        }
        return root.toText();
    }
}
