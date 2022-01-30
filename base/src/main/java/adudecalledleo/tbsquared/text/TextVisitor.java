package adudecalledleo.tbsquared.text;

import java.util.Optional;

@FunctionalInterface
public interface TextVisitor<T, R> {
    Optional<R> visit(TextStyle style, String contents, T data);
}
