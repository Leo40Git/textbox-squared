package adudecalledleo.tbsquared.scene.render;

import java.awt.image.*;
import java.util.Collection;

import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.Scene;

public interface SceneRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();

    BufferedImage renderScene(Scene scene);
}
