package adudecalledleo.tbsquared.parse;

import java.util.Optional;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.Node;
import adudecalledleo.tbsquared.parse.node.TextNode;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.util.Unit;

public final class DOMConverter {
    private DOMConverter() { }

    public static Text toText(Document root, DataTracker ctx) {
        final var tb = new TextBuilder();
        root.visit(DOMConverter::toText0, new ConverterState(tb, ctx));
        return tb.build();
    }

    private record ConverterState(TextBuilder textBuilder,
                                  DataTracker ctx) { }

    private static Optional<Unit> toText0(Node node, ConverterState convState) {
        final var tb = convState.textBuilder();
        final var ctx = convState.ctx();
        if (node instanceof TextNode textNode) {
            tb.append(textNode.getContents());
        }
        // TODO handle more node types as you create them doofus
        return Optional.empty();
    }
}
