package adudecalledleo.tbsquared.parse.node;

public interface NodeSpanTracker {
    NodeSpanTracker NO_OP = new NodeSpanTracker() { };

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
