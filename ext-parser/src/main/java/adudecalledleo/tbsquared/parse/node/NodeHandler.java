package adudecalledleo.tbsquared.parse.node;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public interface NodeHandler<T extends Node> {
    @Nullable T parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                      Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents);
    void convert(NodeConversionContext ctx, T node, TextBuilder tb);

    private int a() {
        return 0;
    }

    default boolean isAttributeBlank(Attribute attr, List<DOMParser.Error> errors) {
        String attrStr = attr.value().trim();
        if (attrStr.isEmpty()) {
            int start = attr.keySpan().start();
            int length;
            if (attr.valueSpan().start() >= 0) {
                length = attr.valueSpan().start() - start;
            } else {
                length = attr.keySpan().length();
            }
            errors.add(new DOMParser.Error(start, length,
                    attr.key() + " cannot be blank"));
            return true;
        }
        return false;
    }
}
