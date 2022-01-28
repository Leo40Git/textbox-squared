package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;
import java.awt.image.*;
import java.util.Collection;

import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.Scene;
import adudecalledleo.tbsquared.scene.render.SceneRenderer;

public record CompositeSceneRenderer(Config config,
                                     SceneImageFactory imageFactory,
                                     TextboxRenderer textboxRenderer,
                                     FaceRenderer faceRenderer,
                                     TextRenderer textRenderer) implements SceneRenderer {
    public record Config(int sceneWidth, int sceneHeight, Color sceneBackground,
                         int textboxX, int textboxY, int textboxWidth, int textboxHeight,
                         int textboxMarginX, int textboxMarginY) { }

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
        BufferedImage image = imageFactory.createSceneImage(config.sceneWidth(), config.sceneHeight());
        var g = image.createGraphics();
        g.setBackground(config.sceneBackground());
        g.clearRect(0, 0, config.sceneWidth(), config.sceneHeight());
        textboxRenderer.renderTextbox(g, scene.metadata(), config.textboxX(), config.textboxY(), config.sceneWidth(), config.sceneHeight()
        );
        int x = config.textboxX() + config.textboxMarginX();
        int y = config.textboxY() + config.textboxMarginY();
        Point faceOffset = faceRenderer.renderFaces(g, scene.faces(), scene.metadata(), x, y);
        x += faceOffset.x;
        y += faceOffset.y;
        textRenderer.renderText(g, scene.textNodes(), scene.metadata(), x, y);
        g.dispose();
        return image;
    }
}
