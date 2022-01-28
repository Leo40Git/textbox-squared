package adudecalledleo.tbsquared.color;

import java.awt.*;

public interface IndexedColorProvider {
    int getColorIndexCount();
    Color getColorByIndex(int index);
}
