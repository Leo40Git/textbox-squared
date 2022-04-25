package adudecalledleo.tbsquared.util.render;

public enum Orientation {
    LEFT_TO_RIGHT(false, 1), RIGHT_TO_LEFT(false, -1),
    TOP_TO_BOTTOM(true, 1), BOTTOM_TO_TOP(true, -1);

    private final boolean vertical;
    private final int increment;

    Orientation(boolean vertical, int increment) {
        this.vertical = vertical;
        this.increment = increment;
    }

    public boolean isVertical() {
        return vertical;
    }

    public int getIncrement() {
        return increment;
    }
}
