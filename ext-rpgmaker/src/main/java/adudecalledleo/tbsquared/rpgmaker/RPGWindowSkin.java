package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import adudecalledleo.tbsquared.text.modifier.color.ArrayIndexedColorProvider;
import adudecalledleo.tbsquared.text.modifier.color.IndexedColorProvider;

public final class RPGWindowSkin {
    public enum Version {
        VX_OR_VXA(32) {
            @Override
            public int scale(int orig) {
                return orig;
            }
        },
        MV_OR_MZ(32) {
            @Override
            public int scale(int orig) {
                return (int) (orig * 1.5);
            }
        };

        private final int indexedColorCount;

        Version(int indexedColorCount) {
            this.indexedColorCount = indexedColorCount;
        }

        public int getIndexedColorCount() {
            return indexedColorCount;
        }

        public int scale(int orig) {
            throw new AbstractMethodError("scale(int) not implemented for " + this);
        }
    }

    private final TextboxRenderer textboxRenderer;
    private final IndexedColorProvider indexedColors;

    public RPGWindowSkin(Version version, BufferedImage windowImage, RPGWindowTint backTint) {
        this.textboxRenderer = new RPGTextboxRenderer(version, windowImage, backTint);

        Color[] colors = new Color[version.getIndexedColorCount()];
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
