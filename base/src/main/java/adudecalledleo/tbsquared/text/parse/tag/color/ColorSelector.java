package adudecalledleo.tbsquared.text.parse.tag.color;

import java.awt.*;

import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;

@FunctionalInterface
public interface ColorSelector {
    DataKey<Palette> PALETTE = new DataKey<>(Palette.class, "palette");

    static ColorSelector parse(String value) {
        return ColorSelectors.parse(value);
    }

    Color getColor(DataTracker ctx);
}
