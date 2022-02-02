package adudecalledleo.tbsquared.parse;

import java.util.List;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.TextNode;

public final class DOMParser {
    private DOMParser() { }

    public static Document parse(String text) {
        // TODO actually implement this!
        return new Document(List.of(new TextNode(text)));
    }
}
