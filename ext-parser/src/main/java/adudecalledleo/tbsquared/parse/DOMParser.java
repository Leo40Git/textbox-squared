package adudecalledleo.tbsquared.parse;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import adudecalledleo.tbsquared.parse.node.Document;
import adudecalledleo.tbsquared.parse.node.NodeParsingContext;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;

public final class DOMParser {
    private DOMParser() { }

    public static Result parse(NodeRegistry registry, SpanTracker spanTracker, String contents) {
        if (contents.isEmpty()) {
            return Result.success(new Document());
        }
        var ctx = new NodeParsingContext(registry, spanTracker);
        var errors = new LinkedList<DOMParser.Error>();
        var result = ctx.parse(DOMInputSanitizer.apply(contents), 0, errors);
        if (errors.isEmpty()) {
            return Result.success(new Document(result));
        } else {
            return Result.failure(errors);
        }
    }

    public static Result parse(NodeRegistry registry, String contents) {
        return parse(registry, SpanTracker.NO_OP, contents);
    }

    public record Error(int start, int end, String message) {
        public int length() {
            return end - start;
        }
    }

    public static final class Result {
        private final Document document;
        private final List<Error> errors;

        private Result(Document document, List<Error> errors) {
            this.document = document;
            this.errors = errors;
        }

        public static Result success(Document document) {
            return new Result(document, List.of());
        }

        public static Result failure(Collection<Error> errors) {
            return new Result(null, List.copyOf(errors));
        }

        public Document document() {
            if (document == null) {
                throw new IllegalStateException("Result is a failure!");
            }
            return document;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public List<Error> errors() {
            return errors;
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
