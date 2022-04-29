package adudecalledleo.tbsquared.parse.node;

import java.util.LinkedHashMap;
import java.util.Map;

import adudecalledleo.tbsquared.text.Span;

public abstract class Node {
    protected final String name;
    protected final Span openingSpan, closingSpan;
    protected final Map<String, Attribute> attributes;

    public Node(String name, Span openingSpan, Span closingSpan, Map<String, Attribute> attributes) {
        this.name = name;
        this.openingSpan = openingSpan;
        this.closingSpan = closingSpan;
        this.attributes = attributes;
    }

    public Node(String name, Span openingSpan, Span closingSpan) {
        this(name, openingSpan, closingSpan, new LinkedHashMap<>());
    }

    public String getName() {
        return name;
    }

    public Span getOpeningSpan() {
        return openingSpan;
    }

    public Span getClosingSpan() {
        return closingSpan;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }
}
