package adudecalledleo.tbsquared.scene;

import java.awt.image.*;
import java.util.Collection;

public interface SceneRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();

    BufferedImage renderScene(Scene scene);
}
