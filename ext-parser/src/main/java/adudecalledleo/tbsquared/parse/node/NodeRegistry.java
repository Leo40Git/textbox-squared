package adudecalledleo.tbsquared.parse.node;

import java.util.HashMap;
import java.util.Map;

import adudecalledleo.tbsquared.parse.node.color.ColorNode;
import adudecalledleo.tbsquared.parse.node.style.BasicStyleNodes;
import adudecalledleo.tbsquared.parse.node.style.StyleNode;
import org.jetbrains.annotations.Nullable;

public final class NodeRegistry {
    private static final NodeRegistry DEFAULT;

    static {
        DEFAULT = new NodeRegistry();
        DEFAULT.registerDefaults();
        DEFAULT.freeze();
    }

    public static NodeRegistry getDefault() {
        return DEFAULT;
    }

    private Map<String, NodeHandler<?>> handlers;

    private NodeRegistry(Map<String, NodeHandler<?>> handlers) {
        this.handlers = handlers;
        register(Document.NAME, Document.HANDLER);
        register(TextNode.NAME, TextNode.HANDLER);
    }

    public NodeRegistry() {
        this(new HashMap<>());
    }

    public void registerDefaults() {
        BasicStyleNodes.register(this);
        ColorNode.register(this);
        StyleNode.register(this);
    }

    public void register(String name, NodeHandler<?> handler) {
        if (handlers.containsKey(name)) {
            throw new IllegalArgumentException("Node \"" + name + "\" is already registered");
        }
        handlers.put(name, handler);
    }

    public @Nullable NodeHandler<?> getHandler(String name) {
        return handlers.get(name);
    }

    public void freeze() {
        handlers = Map.copyOf(handlers);
    }
}
