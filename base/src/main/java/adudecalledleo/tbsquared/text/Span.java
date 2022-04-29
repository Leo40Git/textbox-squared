package adudecalledleo.tbsquared.text;

public record Span(int start, int length) {
    public static final Span INVALID = new Span(-1, -1);

    public boolean isValid() {
        return start > 0 && length > 0;
    }
}
