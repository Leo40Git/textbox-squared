package adudecalledleo.tbsquared.color;

import java.awt.*;

public interface Palette {
    int getSize();
    Color getColor(int index);
    Color[] getAllColors();
}
