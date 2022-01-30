package adudecalledleo.tbsquared.text;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class Text {
    public static final Text EMPTY = new Text(TextStyle.EMPTY, "");

    private final TextStyle style;
    private final String contents;
    private final List<Text> children;

    public Text(TextStyle style, String contents, Collection<Text> children) {
        this.style = style;
        this.contents = contents;
        this.children = List.copyOf(children);
    }

    public Text(TextStyle style, String contents) {
        this(style, contents, List.of());
    }

    public TextStyle getStyle() {
        return style;
    }

    public String getContents() {
        return contents;
    }

    public List<Text> getChildren() {
        return children;
    }

    public <T, R> Optional<R> visitSelf(TextVisitor<T, R> visitor, T data) {
        return visitor.visit(style, contents, data);
    }

    public <T, R> Optional<R> visit(TextVisitor<T, R> visitor, T data) {
        var result = visitSelf(visitor, data);
        if (result.isPresent()) {
            return result;
        }
        for (var child : children) {
            result = child.visit(visitor, data);
            if (result.isPresent()) {
                return result;
            }
        }
        return result;
    }
}
