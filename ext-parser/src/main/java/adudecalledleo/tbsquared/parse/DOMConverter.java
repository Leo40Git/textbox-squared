package adudecalledleo.tbsquared.parse;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.NodeConversionContext;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class DOMConverter {
    private DOMConverter() { }

    public static Text toText(Document root, DataTracker metadata) {
        final var tb = new TextBuilder();
        final var ctx = new NodeConversionContext(metadata);
        ctx.convert(root, tb);
        return tb.build();
    }
}
