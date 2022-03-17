package adudecalledleo.tbsquared.parse;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.NodeParsingContext;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;
import adudecalledleo.tbsquared.parse.node.NodeSpanTracker;

public final class DOMParser {
    private DOMParser() { }

    public static Document parse(NodeRegistry registry, NodeSpanTracker spanTracker, String contents) {
        var ctx = new NodeParsingContext(registry, spanTracker);
        return new Document(ctx.parse(DOMInputSanitizer.apply(contents)));
    }

    public static Document parse(NodeRegistry registry, String contents) {
        return parse(registry, NodeSpanTracker.NO_OP, contents);
    }
}
