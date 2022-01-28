package adudecalledleo.tbsquared.render.composite;

import java.awt.*;
import java.awt.image.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.render.FacePosition;
import adudecalledleo.tbsquared.render.SceneRenderer;
import adudecalledleo.tbsquared.text.node.NodeList;

public record CompositeSceneRenderer(SceneConfiguration config,
                                     SceneImageFactory imageFactory,
                                     TextboxRenderer textboxRenderer,
                                     FaceRenderer faceRenderer,
                                     TextRenderer textRenderer) implements SceneRenderer {
    @Override
    public Collection<? extends FacePosition> getFacePositions() {
        return faceRenderer.getFacePositions();
    }

    @Override
    public FacePosition getDefaultFacePosition() {
        return faceRenderer.getDefaultFacePosition();
    }

    @Override
    public BufferedImage renderScene(Map<FacePosition, Face> faces, NodeList nodes) {
        BufferedImage image = imageFactory.createSceneImage(config.sceneWidth(), config.sceneHeight());
        var g = image.createGraphics();
        g.setBackground(config.sceneBackground());
        g.clearRect(0, 0, config.sceneWidth(), config.sceneHeight());
        textboxRenderer.renderTextbox(g, config.textboxX(), config.textboxY(), config.sceneWidth(), config.sceneHeight());
        int x = config.textboxX() + config.textboxMarginX();
        int y = config.textboxY() + config.textboxMarginY();
        Point faceOffset = faceRenderer.renderFaces(g, faces, x, y);
        x += faceOffset.x;
        y += faceOffset.y;
        textRenderer.renderText(g, nodes, x, y);
        g.dispose();
        return image;
    }
}
