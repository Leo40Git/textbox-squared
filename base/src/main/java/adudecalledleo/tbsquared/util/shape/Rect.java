package adudecalledleo.tbsquared.util.shape;

public record Rect(int x, int y, int width, int height) {
    public int x1() {
        return this.x;
    }

    public int x2() {
        return this.x + this.width;
    }

    public int y1() {
        return this.y;
    }

    public int y2() {
        return this.y + this.height;
    }
}
