package adudecalledleo.tbsquared.parse.node.style;

import adudecalledleo.tbsquared.parse.node.NodeRegistry;

public final class BasicStyleNodes {
    private BasicStyleNodes() { }

    public static void register(NodeRegistry registry) {
        registry.register(BoldNode.NAME, BoldNode.HANDLER);
        registry.register(ItalicNode.NAME, ItalicNode.HANDLER);
        registry.register(UnderlineNode.NAME, UnderlineNode.HANDLER);
        registry.register(StrikethroughNode.NAME, StrikethroughNode.HANDLER);
        registry.register(SuperscriptNode.NAME, SuperscriptNode.HANDLER);
        registry.register(SubscriptNode.NAME, SubscriptNode.HANDLER);
    }
}
