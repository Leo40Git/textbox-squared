package adudecalledleo.tbsquared.app.plugin.api.renderer;

import java.util.Optional;

import adudecalledleo.tbsquared.app.plugin.api.config.ConfigSpec;
import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import org.pf4j.ExtensionPoint;

public interface SceneRendererProvider extends ExtensionPoint {
    String getName();
    ConfigSpec getConfigSpec();
    SceneRenderer createSceneRenderer(DataTracker config);

    // for textbox editor UI
    BackgroundRenderer getTextboxBackgroundRenderer();
    FontProvider getTextboxFonts();
    Optional<Palette> getTextboxPalette();
}
