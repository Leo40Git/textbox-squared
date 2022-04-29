package adudecalledleo.tbsquared.parse.node;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;

public abstract class ImplicitNodeHandler<T extends Node> implements NodeHandler<T> {
    protected final String name;

    public ImplicitNodeHandler(String name) {
        this.name = name;
    }

    @Override
    public final T parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                         Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
        throw new UnsupportedOperationException("Explicit [%s] declaration not allowed!".formatted(name));
    }

    @Override
    public abstract void convert(NodeConversionContext ctx, T node, TextBuilder tb);
}
