package adudecalledleo.tbsquared.util.render;

import java.awt.*;

public final class Colors {
    private Colors() { }

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Color darker(Color original, double factor) {
        return new Color(Math.max((int) (original.getRed() * factor), 0),
                Math.max((int) (original.getGreen() * factor), 0),
                Math.max((int) (original.getBlue() * factor), 0),
                original.getAlpha());
    }

    public static Color brighter(Color original, double factor) {
        int r = original.getRed();
        int g = original.getGreen();
        int b = original.getBlue();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non-pure color (non-zero RGB) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, original.getAlpha());
        }

        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / factor), 255),
                Math.min((int) (g / factor), 255),
                Math.min((int) (b / factor), 255),
                original.getAlpha());
    }
}
