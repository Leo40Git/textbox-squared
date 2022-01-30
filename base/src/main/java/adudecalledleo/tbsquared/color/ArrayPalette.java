package adudecalledleo.tbsquared.color;

import java.awt.*;

@SuppressWarnings("ClassCanBeRecord")
public final class ArrayPalette implements Palette {
    private final Color[] colors;

    public ArrayPalette(Color[] colors) {
        this.colors = colors;
    }

    @Override
    public int getSize() {
        return colors.length;
    }

    @Override
    public Color getColor(int index) {
        return colors[index];
    }

    @Override
    public Color[] getAllColors() {
        return colors.clone();
    }
}
