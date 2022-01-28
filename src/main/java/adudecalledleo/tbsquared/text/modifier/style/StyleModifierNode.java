package adudecalledleo.tbsquared.text.modifier.style;

import java.util.Arrays;

import adudecalledleo.tbsquared.metadata.MetadataTracker;
import adudecalledleo.tbsquared.text.modifier.ModifierParser;
import adudecalledleo.tbsquared.text.node.ModifierNode;
import adudecalledleo.tbsquared.text.node.NodeList;
import adudecalledleo.tbsquared.util.Span;

import static adudecalledleo.tbsquared.text.modifier.ModifierParser.modLen;

public final class StyleModifierNode extends ModifierNode {
    public static final char KEY = 's';

    private final StyleSpec spec;

    public StyleModifierNode(int start, int length, StyleSpec spec, Span... argSpans) {
        super(start, length, KEY, argSpans);
        this.spec = spec;
    }

    public StyleSpec getSpec() {
        return spec;
    }

    public static final class Parser implements ModifierParser {
        public static final String ERROR_PREFIX = "Style modifier: ";

        @Override
        public void parse(MetadataTracker ctx, int start, int argsStart, String args, NodeList nodes) {
            nodes.add(new StyleModifierNode(start, modLen(args),
                    StyleSpec.fromModArgs(ERROR_PREFIX, argsStart, args, nodes),
                    new Span(argsStart, args.length())));
        }
    }

    @Override
    public String toString() {
        return "StyleModifierNode{" +
                "spec=" + spec +
                ", start=" + start +
                ", length=" + length +
                ", argSpans=" + Arrays.toString(argSpans) +
                '}';
    }
}
