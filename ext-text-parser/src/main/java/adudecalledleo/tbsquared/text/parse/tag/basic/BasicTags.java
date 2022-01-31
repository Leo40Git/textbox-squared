package adudecalledleo.tbsquared.text.parse.tag.basic;

import adudecalledleo.tbsquared.text.parse.tag.TagRegistry;

public final class BasicTags {
    public static final String BOLD = "b";
    public static final String ITALIC = "i";
    public static final String UNDERLINE = "u";
    public static final String STRIKETHROUGH = "s";

    private BasicTags() { }

    public static void register() {
        TagRegistry.register(BOLD, BTag::new);
        TagRegistry.register(ITALIC, ITag::new);
        TagRegistry.register(UNDERLINE, UTag::new);
        TagRegistry.register(STRIKETHROUGH, STag::new);
    }
}
