package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.color.ArrayPalette;
import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.scene.composite.CompositeSceneRenderer;
import adudecalledleo.tbsquared.scene.composite.TextRenderer;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import org.intellij.lang.annotations.MagicConstant;

public final class RPGWindowSkin {
    public static final int TEXTBOX_BORDER_IN_BACKGROUND = 1 << 0;
    public static final int TEXTBOX_ARROW_IN_BACKGROUND = 1 << 1;
    public static final int TEXT_NO_OUTLINE = 1 << 2;

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
    private final Palette palette;
    private final TextRenderer textRenderer;

    public RPGWindowSkin(Version version, BufferedImage windowImage, RPGWindowTint backTint,
                         @MagicConstant(flags = {
                                 RPGWindowSkin.TEXTBOX_BORDER_IN_BACKGROUND,
                                 RPGWindowSkin.TEXTBOX_ARROW_IN_BACKGROUND,
                                 RPGWindowSkin.TEXT_NO_OUTLINE
                         }) int flags) {
        this.version = version;
        this.textboxRenderer = new RPGTextboxRenderer(version, windowImage, backTint, flags);

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
        palette = new ArrayPalette(colors);
        textRenderer = new RPGTextRenderer(colors[0], flags);
    }

    public Version getVersion() {
        return version;
    }

    public int getTextboxPadding() {
        return version.textboxPadding();
    }

    public TextboxRenderer getTextboxRenderer() {
        return textboxRenderer;
    }

    public Palette getPalette() {
        return palette;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public CompositeSceneRenderer.Builder sceneRendererBuilder() {
        return CompositeSceneRenderer.builder()
                .textboxPadding(version.textboxPadding(), version.textboxPadding())
                .textboxRenderer(textboxRenderer)
                .faceRenderer(RPGFaceRenderer.INSTANCE)
                .textRenderer(textRenderer);
    }
}
