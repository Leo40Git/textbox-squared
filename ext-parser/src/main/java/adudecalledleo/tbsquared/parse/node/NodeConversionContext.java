package adudecalledleo.tbsquared.parse.node;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;

public record NodeConversionContext(DataTracker metadata) {
    @SuppressWarnings("unchecked")
    public void convert(Node node, TextBuilder tb) {
        var handler = (NodeHandler<Node>) NodeRegistry.get(node.getName());
        if (handler == null) {
            throw new IllegalStateException("Unknown node \"" + node.getName() + "\"");
        }
        handler.convert(this, node, tb);
    }

    public void convert(Iterable<Node> nodes, TextBuilder tb) {
        for (var node : nodes) {
            convert(node, tb);
        }
    }
}
