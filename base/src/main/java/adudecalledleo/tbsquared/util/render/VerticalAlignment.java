package adudecalledleo.tbsquared.util.render;

public enum VerticalAlignment {
    TOP, CENTER, BOTTOM;

    public int align(int containerHeight, int height) {
        return switch (this) {
            case TOP -> 0;
            case CENTER -> containerHeight / 2 - height / 2;
            case BOTTOM -> containerHeight - height;
        };
    }
}
