package adudecalledleo.tbsquared.parse;

import java.util.LinkedList;
import java.util.List;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.NodeParsingContext;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;

public final class DOMParser {
    private DOMParser() { }

    public static Result parse(NodeRegistry registry, String contents, DataTracker metadata, SpanTracker spanTracker) {
        if (contents.isEmpty()) {
            return new Result(new Document(), List.of());
        }
        var ctx = new NodeParsingContext(registry, metadata, spanTracker);
        var errors = new LinkedList<DOMParser.Error>();
        var result = ctx.parse(DOMInputSanitizer.apply(contents), 0, errors);
        return new Result(new Document(result), errors);
    }

    public static Result parse(NodeRegistry registry, String contents, DataTracker metadata) {
        return parse(registry, contents, metadata, SpanTracker.NO_OP);
    }

    public record Error(int start, int length, String message) {
        public int end() {
            return start + length;
        }
    }

    public record Result(Document document, List<Error> errors) {
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }

    public interface SpanTracker {
        SpanTracker NO_OP = new SpanTracker() { };

        default void markEscaped(int start, int end) { }
        default void markNodeDecl(String node, int start, int end) { }
        default void markNodeDeclOpening(String node, int start, int end) {
            markNodeDecl(node, start, end);
        }
        default void markNodeDeclClosing(String node, int start, int end) {
            markNodeDecl(node, start, end);
        }
        default void markNodeDeclAttribute(String node, String key, int keyStart, int keyEnd, String value, int valueStart, int valueEnd) { }
    }
}
