package adudecalledleo.tbsquared.parse;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.NodeParsingContext;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;

public final class DOMParser {
    private DOMParser() { }

    public static Document parse(NodeRegistry registry, String contents) {
        var ctx = new NodeParsingContext(registry);
        return new Document(ctx.parse(DOMInputSanitizer.apply(contents)));
    }
}
