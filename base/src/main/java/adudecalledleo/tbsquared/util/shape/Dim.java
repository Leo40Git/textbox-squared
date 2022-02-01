package adudecalledleo.tbsquared.util.shape;

public record Dim(int width, int height) {
    public static final Dim ZERO = new Dim(0, 0);
}
