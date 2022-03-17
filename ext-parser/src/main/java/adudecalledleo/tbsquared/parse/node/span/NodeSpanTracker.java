package adudecalledleo.tbsquared.parse.node.span;

public interface NodeSpanTracker {
    NodeSpanTracker NO_OP = new NodeSpanTracker() { };

    default void markEscape(int start, int end) { }
    default void markNodeDeclaration(String node, NodeDeclarationType type, int start, int end) { }
    default void markNodeAttribute(String node, String key, int keyStart, int keyEnd, String value, int valueStart, int valueEnd) { }

}
