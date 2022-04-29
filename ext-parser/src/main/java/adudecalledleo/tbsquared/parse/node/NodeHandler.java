package adudecalledleo.tbsquared.parse.node;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public interface NodeHandler<T extends Node> {
    T parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
            Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents);
    void convert(NodeConversionContext ctx, T node, TextBuilder tb);
}
