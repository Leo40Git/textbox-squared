package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.color.ArrayIndexedColorProvider;
import adudecalledleo.tbsquared.color.IndexedColorProvider;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.scene.composite.CompositeSceneRenderer;
import adudecalledleo.tbsquared.scene.composite.TextRenderer;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.DefaultSceneImageFactory;
import adudecalledleo.tbsquared.util.render.HorizontalAlignment;
import adudecalledleo.tbsquared.util.render.VerticalAlignment;

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

    private final Version version;
    private final TextboxRenderer textboxRenderer;
    private final IndexedColorProvider indexedColors;
    private final TextRenderer textRenderer;

    public RPGWindowSkin(Version version, BufferedImage windowImage, RPGWindowTint backTint) {
        this.version = version;
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
        textRenderer = new RPGTextRenderer(colors[0]);
    }

    public TextboxRenderer getTextboxRenderer() {
        return textboxRenderer;
    }

    public IndexedColorProvider getIndexedColors() {
        return indexedColors;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public CompositeSceneRenderer.Config createSceneRendererConfig(int sceneWidth, int sceneHeight, Color sceneBackground,
                                                                   int textboxWidth, int textboxHeight) {
        return CompositeSceneRenderer.config(sceneWidth, sceneHeight, sceneBackground,
                HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM,
                textboxWidth, textboxHeight,
                version.textboxPadding(), version.textboxPadding());
    }

    public SceneRenderer createSceneRenderer(int sceneWidth, int sceneHeight, Color sceneBackground,
                                             int textboxWidth, int textboxHeight,
                                             FontProvider fonts) {
        return new CompositeSceneRenderer(
                createSceneRendererConfig(sceneWidth, sceneHeight, sceneBackground, textboxWidth, textboxHeight),
                DefaultSceneImageFactory.INSTANCE,
                fonts,
                textboxRenderer,
                RPGFaceRenderer.INSTANCE,
                textRenderer
        );
    }
}
