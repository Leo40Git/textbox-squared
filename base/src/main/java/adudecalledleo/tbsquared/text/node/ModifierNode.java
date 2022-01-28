package adudecalledleo.tbsquared.text.node;

import java.util.Arrays;

import adudecalledleo.tbsquared.util.Span;

public non-sealed abstract class ModifierNode extends Node {
    protected final char key;
    protected final Span[] argSpans;

    public ModifierNode(int start, int length, char key, Span... argSpans) {
        super(start, length);
        this.key = key;
        this.argSpans = argSpans;
    }

    public final char getKey() {
        return key;
    }

    public final Span[] getArgSpans() {
        return argSpans.clone();
    }

    @Override
    public String toString() {
        return "ModifierNode{" +
                "start=" + start +
                ", length=" + length +
                ", key=" + key +
                ", argSpans=" + Arrays.toString(argSpans) +
                '}';
    }
}
