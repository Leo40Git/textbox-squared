package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import adudecalledleo.tbsquared.text.modifier.color.ArrayIndexedColorProvider;
import adudecalledleo.tbsquared.text.modifier.color.IndexedColorProvider;

public final class RPGWindowSkin {
    public enum Version {
        VX, MV, MZ;

        public int scale(int orig) {
            return switch (this) {
                case VX -> orig;
                case MV, MZ -> (int) (orig * 1.5);
            };
        }

        public int textboxMargin() {
            return switch (this) {
                case VX -> 2;
                case MV, MZ -> 4;
            };
        }

        public int textboxPadding() {
            return switch (this) {
                case MV -> 18;
                case VX, MZ -> 12;
            };
        }
    }

    private static final int COLOR_COUNT = 32;

    private final TextboxRenderer textboxRenderer;
    private final IndexedColorProvider indexedColors;

    public RPGWindowSkin(Version version, BufferedImage windowImage, RPGWindowTint backTint) {
        this.textboxRenderer = new RPGTextboxRenderer(version, windowImage, backTint);

        Color[] colors = new Color[COLOR_COUNT];
        final int colorSize = version.scale(8);
        final int colorStartX = version.scale(64), colorStartY = version.scale(96);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                colors[y * 8 + x] = new Color(
                        windowImage.getRGB(colorStartX + (x * colorSize), colorStartY + (y * colorSize)),
                        false);
            }
        }
        indexedColors = new ArrayIndexedColorProvider(colors);
    }

    public TextboxRenderer getTextboxRenderer() {
        return textboxRenderer;
    }

    public IndexedColorProvider getIndexedColors() {
        return indexedColors;
    }
}
