package adudecalledleo.tbsquared.parse.node;

import java.util.Map;

import adudecalledleo.tbsquared.text.TextBuilder;

public abstract class ImplicitNodeHandler<T extends Node> implements NodeHandler<T> {
    protected final String name;

    public ImplicitNodeHandler(String name) {
        this.name = name;
    }

    @Override
    public final T parse(Map<String, String> attributes) {
        throw new UnsupportedOperationException("Explicit [%s] declaration not allowed!".formatted(name));
    }

    @Override
    public abstract void convert(NodeConversionContext ctx, T node, TextBuilder tb);
}
