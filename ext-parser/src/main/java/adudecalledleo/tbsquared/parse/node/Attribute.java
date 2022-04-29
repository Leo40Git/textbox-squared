package adudecalledleo.tbsquared.parse.node;

import adudecalledleo.tbsquared.text.Span;

public record Attribute(String key, Span keySpan, String value, Span valueSpan) { }
