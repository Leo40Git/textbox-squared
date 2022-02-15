package adudecalledleo.tbsquared.text;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class LiteralText implements Text {
    public static final Text EMPTY = new LiteralText(TextStyle.EMPTY, "");

    private final TextStyle style;
    private final String contents;
    private final List<Text> children;

    public LiteralText(TextStyle style, String contents, Collection<Text> children) {
        this.style = style;
        this.contents = contents;
        this.children = List.copyOf(children);
    }

    public LiteralText(TextStyle style, String contents, Text[] children) {
        this(style, contents, List.of(children));
    }

    public LiteralText(TextStyle style, String contents) {
        this(style, contents, List.of());
    }

    @Override
    public TextStyle getStyle() {
        return style;
    }

    @Override
    public String getContents() {
        return contents;
    }

    @Override
    public List<Text> getChildren() {
        return children;
    }

    @Override
    public <T, R> Optional<R> visitSelf(TextVisitor<T, R> visitor, T data) {
        return visitor.visit(style, contents, data);
    }
}
