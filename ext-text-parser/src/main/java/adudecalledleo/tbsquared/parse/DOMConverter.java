package adudecalledleo.tbsquared.parse;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class DOMConverter {
    private DOMConverter() { }

    public static Text toText(Document root) {
        final var tb = new TextBuilder();
        // TODO
        tb.append("TODO!!!!");
        return tb.build();
    }
}
