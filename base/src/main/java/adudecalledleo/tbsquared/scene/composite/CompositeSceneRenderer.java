package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.awt.image.*;
import java.util.Collection;

import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.Scene;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.util.render.HorizontalAlignment;
import adudecalledleo.tbsquared.util.render.VerticalAlignment;

public record CompositeSceneRenderer(Config config,
                                     SceneImageFactory imageFactory,
                                     FontProvider fonts,
                                     TextboxRenderer textboxRenderer,
                                     FaceRenderer faceRenderer,
                                     TextRenderer textRenderer) implements SceneRenderer {
    public record Config(Dimension sceneSize, Color sceneBackground,
                         Rectangle textboxRect,
                         Dimension textboxPadding) { }

    public static Config config(int sceneWidth, int sceneHeight, Color sceneBackground,
                                int textboxX, int textboxY,
                                int textboxWidth, int textboxHeight,
                                int textboxPaddingX, int textboxPaddingY) {
        return new Config(new Dimension(sceneWidth, sceneHeight), sceneBackground,
                new Rectangle(textboxX, textboxY, textboxWidth, textboxHeight),
                new Dimension(textboxPaddingX, textboxPaddingY));
    }

    public static Config config(int sceneWidth, int sceneHeight, Color sceneBackground,
                                HorizontalAlignment textboxAlignX, VerticalAlignment textboxAlignY,
                                int textboxWidth, int textboxHeight,
                                int textboxPaddingX, int textboxPaddingY) {
        return config(sceneWidth, sceneHeight, sceneBackground,
                textboxAlignX.align(sceneWidth, textboxWidth), textboxAlignY.align(sceneHeight, textboxHeight),
                textboxWidth, textboxHeight,
                textboxPaddingX, textboxPaddingY);
    }

    @Override
    public Collection<? extends FacePosition> getFacePositions() {
        return faceRenderer.getFacePositions();
    }

    @Override
    public FacePosition getDefaultFacePosition() {
        return faceRenderer.getDefaultFacePosition();
    }

    @Override
    public BufferedImage renderScene(Scene scene) {
        BufferedImage image = imageFactory.createSceneImage(config.sceneSize().width, config.sceneSize().height);
        var g = image.createGraphics();
        g.setBackground(config.sceneBackground());
        g.clearRect(0, 0, config.sceneSize().width, config.sceneSize().height);
        textboxRenderer.renderTextbox(g, scene.metadata(),
                config.textboxRect().x, config.textboxRect().y,
                config.textboxRect().width, config.textboxRect().height);
        int x = config.textboxRect().x + config.textboxPadding().width;
        int y = config.textboxRect().y + config.textboxPadding().height;
        Dimension facePadding = faceRenderer.renderFaces(g, scene.faces(), scene.metadata(), x, y);
        x += facePadding.width;
        y += facePadding.height;
        textRenderer.renderText(g, scene.text(), fonts, scene.metadata(), x, y);
        g.dispose();
        return image;
    }
}
