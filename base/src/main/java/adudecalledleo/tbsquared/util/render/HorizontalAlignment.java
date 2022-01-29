package adudecalledleo.tbsquared.util.render;

public enum HorizontalAlignment {
    LEFT, CENTER, RIGHT;

    public int align(int containerWidth, int width) {
        return switch(this) {
            case LEFT -> 0;
            case CENTER -> containerWidth / 2 - width / 2;
            case RIGHT -> containerWidth - width;
        };
    }
}
