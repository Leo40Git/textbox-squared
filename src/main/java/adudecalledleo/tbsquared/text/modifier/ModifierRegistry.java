package adudecalledleo.tbsquared.text.modifier;

import java.util.HashMap;
import java.util.Map;

import adudecalledleo.tbsquared.text.modifier.style.StyleModifierNode;

public final class ModifierRegistry {
    private static final Map<Character, ModifierParser> MAP = new HashMap<>();

    private ModifierRegistry() { }

    public static void init() {
        register(StyleModifierNode.KEY, new StyleModifierNode.Parser());
        register(ColorModifierNode.KEY, new ColorModifierNode.Parser());
    }

    public static void register(char c, ModifierParser parser) {
        if (MAP.containsKey(c)) {
            throw new IllegalStateException("Tried to register modifier with already-used key '" + c + "'!");
        }
        MAP.put(c, parser);
    }

    public static ModifierParser get(char c) {
        return MAP.get(c);
    }
}
