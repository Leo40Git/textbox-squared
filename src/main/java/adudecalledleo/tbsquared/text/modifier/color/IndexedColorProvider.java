package adudecalledleo.tbsquared.text.modifier.color;

import java.awt.*;

public interface IndexedColorProvider {
    int getColorIndexCount();
    Color getColorByIndex(int index);
    Color[] getAllColors();
}
