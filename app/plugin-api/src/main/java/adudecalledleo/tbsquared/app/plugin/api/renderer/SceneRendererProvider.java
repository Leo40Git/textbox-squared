package adudecalledleo.tbsquared.app.plugin.api.renderer;

import java.awt.*;
import java.util.Optional;

import adudecalledleo.tbsquared.app.plugin.api.config.ConfigSpec;
import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DataKey;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.FacePool;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.icon.IconPool;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import org.pf4j.ExtensionPoint;

public interface SceneRendererProvider extends ExtensionPoint {
    String getName();
    ConfigSpec getConfigSpec();
    SceneRenderer createSceneRenderer(DataTracker config);

    DataTracker getMetadata();

    DataKey<FacePool> FACES = new DataKey<>(FacePool.class, "faces");
    DataKey<BackgroundRenderer> BACKGROUND_RENDERER = new DataKey<>(BackgroundRenderer.class, "background_renderer");
    DataKey<FontProvider> FONTS = new DataKey<>(FontProvider.class, "fonts");
    DataKey<Color> TEXT_COLOR = new DataKey<>(Color.class, "text_color");
    DataKey<Palette> PALETTE = new DataKey<>(Palette.class, "palette");
    DataKey<IconPool> ICONS = new DataKey<>(IconPool.class, "icons");

    default FacePool getFaces() {
        return getMetadata().get(FACES).orElseThrow(() ->
                new IllegalStateException("Missing required metadata " + FACES));
    }
    default BackgroundRenderer getTextboxBackgroundRenderer() {
        return getMetadata().get(BACKGROUND_RENDERER).orElseThrow(() ->
                new IllegalStateException("Missing required metadata " + BACKGROUND_RENDERER));
    }
    default FontProvider getTextboxFonts() {
        return getMetadata().get(FONTS).orElseThrow(() ->
                new IllegalStateException("Missing required metadata " + FONTS));
    }
    default Color getTextboxTextColor() {
        return getMetadata().get(TEXT_COLOR).orElseThrow(() ->
                new IllegalStateException("Missing required metadata " + TEXT_COLOR));
    }
    default Optional<Palette> getTextboxPalette() {
        return getMetadata().get(PALETTE);
    }

    default Optional<IconPool> getIcons() {
        return getMetadata().get(ICONS);
    }
}
