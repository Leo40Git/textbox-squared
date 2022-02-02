package adudecalledleo.tbsquared.parse;

import java.util.Optional;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.Node;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.util.Unit;

public final class DOMConverter {
    private DOMConverter() { }

    public static Text toText(Document root) {
        final var tb = new TextBuilder();
        root.visit(DOMConverter::toText0, tb);
        return tb.build();
    }

    private static Optional<Unit> toText0(Node node, TextBuilder tb) {
        // TODO
        return Optional.empty();
    }
}
