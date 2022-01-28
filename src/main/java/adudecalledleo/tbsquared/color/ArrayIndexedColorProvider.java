package adudecalledleo.tbsquared.color;

import java.awt.*;

@SuppressWarnings("ClassCanBeRecord")
public final class ArrayIndexedColorProvider implements IndexedColorProvider {
    private final Color[] colors;

    public ArrayIndexedColorProvider(Color[] colors) {
        this.colors = colors;
    }

    @Override
    public int getColorIndexCount() {
        return colors.length;
    }

    @Override
    public Color getColorByIndex(int index) {
        return colors[index];
    }
}
