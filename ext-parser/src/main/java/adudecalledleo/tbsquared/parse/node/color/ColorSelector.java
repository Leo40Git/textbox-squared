package adudecalledleo.tbsquared.parse.node.color;

import java.awt.*;

import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ColorSelector {
    DataKey<Palette> PALETTE = new DataKey<>(Palette.class, "palette");

    static ColorSelector parse(String value) {
        return ColorSelectors.parse(value);
    }

    @Nullable Color getColor(DataTracker ctx);
}
