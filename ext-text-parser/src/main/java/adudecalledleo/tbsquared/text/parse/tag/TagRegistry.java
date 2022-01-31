package adudecalledleo.tbsquared.text.parse.tag;

import java.util.HashMap;
import java.util.Map;

import adudecalledleo.tbsquared.text.parse.tag.basic.BasicTags;
import adudecalledleo.tbsquared.text.parse.tag.color.ColorTag;

public final class TagRegistry {
    private TagRegistry() { }

    private static final Map<String, TagFactory> MAP = new HashMap<>();

    public static void register(String name, TagFactory factory) {
        if (MAP.containsKey(name)) {
            throw new IllegalArgumentException("Tag with name \"" + name + "\" is already registered!");
        }
        MAP.put(name, factory);
    }

    public static TagFactory get(String name) {
        return MAP.get(name);
    }

    static {
        BasicTags.register();
        register("adudecalledleo/tbsquared/text/parse/tag/color", ColorTag::new);
    }
}
