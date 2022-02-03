package adudecalledleo.tbsquared.parse.node;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public final class NodeRegistry {
    private static final NodeRegistry DEFAULT;

    static {
        DEFAULT = new NodeRegistry();
        DEFAULT.registerDefaults();
        DEFAULT.handlers = Map.copyOf(DEFAULT.handlers);
    }

    public static NodeRegistry getDefault() {
        return DEFAULT;
    }

    private Map<String, NodeHandler<?>> handlers;

    private NodeRegistry(Map<String, NodeHandler<?>> handlers) {
        this.handlers = handlers;
    }

    public NodeRegistry() {
        this(new HashMap<>());
    }

    public void registerDefaults() {
        register(Document.NAME, Document.HANDLER);
        register(TextNode.NAME, TextNode.HANDLER);
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
}
