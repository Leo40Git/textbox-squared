package adudecalledleo.tbsquared.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import adudecalledleo.tbsquared.util.Pair;

public final class TextBuilder {
    private final StringBuilder deferredContents;
    private final List<TextStyle> styleStack;
    private final List<Pair<TextStyle, String>> committedTexts;
    private TextStyle deferredStyle;

    public TextBuilder() {
        this.deferredContents = new StringBuilder();
        this.styleStack = new ArrayList<>();
        this.committedTexts = new ArrayList<>();
        this.deferredStyle = TextStyle.EMPTY;
    }

    private void commitDeferred() {
        if (!deferredContents.isEmpty()) {
            committedTexts.add(new Pair<>(deferredStyle, deferredContents.toString()));
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

    public TextBuilder pushStyle(TextStyle style) {
        if (!this.deferredStyle.equals(style)) {
            commitDeferred();
            styleStack.add(0, deferredStyle);
            this.deferredStyle = style;
        }
        return this;
    }

    public TextBuilder pushStyle(UnaryOperator<TextStyle> styleModifier) {
        return pushStyle(styleModifier.apply(deferredStyle));
    }

    public TextBuilder popStyle() {
        return style(styleStack.remove(0));
    }

    public TextStyle getStyle() {
        return deferredStyle;
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

    private static Text commit2Text(Pair<TextStyle, String> commit) {
        return new LiteralText(commit.left(), commit.right());
    }

    public Text build() {
        commitDeferred();
        final int commitCount = committedTexts.size();
        if (commitCount <= 0) {
            return LiteralText.EMPTY;
        } else if (commitCount == 1) {
            return commit2Text(committedTexts.get(0));
        } else {
            // commit 0 is the "root commit", and is converted into the Text we return
            // all subsequent commits are converted into children of our returned Text
            Text[] children = new Text[commitCount - 1];
            for (int i = 0; i < children.length; i++) {
                children[i] = commit2Text(committedTexts.get(i + 1));
            }
            var rootCommit = committedTexts.get(0);
            return new LiteralText(rootCommit.left(), rootCommit.right(), children);
        }
    }
}
