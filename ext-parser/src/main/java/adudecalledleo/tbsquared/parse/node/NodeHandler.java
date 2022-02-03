package adudecalledleo.tbsquared.parse.node;

import java.util.Map;

import adudecalledleo.tbsquared.text.TextBuilder;

public interface NodeHandler<T extends Node> {
    T parse(Map<String, String> attributes);
    void convert(NodeConversionContext ctx, T node, TextBuilder tb);
}
