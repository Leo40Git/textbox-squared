package adudecalledleo.tbsquared.parse.node;

import java.util.HashMap;
import java.util.Map;

import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public final class NodeRegistry {
    private NodeRegistry() { }

    private static final Map<String, NodeHandler<?>> HANDLERS = new HashMap<>();

    public static void register(String name, NodeHandler<?> handler) {
        if (HANDLERS.containsKey(name)) {
            throw new IllegalArgumentException("Node \"" + name + "\" is already registered");
        }
        HANDLERS.put(name, handler);
    }

    public static @Nullable NodeHandler<?> get(String name) {
        return HANDLERS.get(name);
    }

    static {
        register(Document.NAME, new ImplicitNodeHandler<Document>(Document.NAME) {
            @Override
            public void convert(NodeConversionContext ctx, Document node, TextBuilder tb) {
                ctx.convert(node.getChildren(), tb);
            }
        });
        register(TextNode.NAME, new ImplicitNodeHandler<TextNode>(TextNode.NAME) {
            @Override
            public void convert(NodeConversionContext ctx, TextNode node, TextBuilder tb) {
                tb.append(node.getContents());
            }
        });
    }
}
