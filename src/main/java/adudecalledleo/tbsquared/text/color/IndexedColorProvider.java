package adudecalledleo.tbsquared.text.color;

import java.awt.*;

public interface IndexedColorProvider {
    int getColorIndexCount();
    Color getColorByIndex(int index);
}
