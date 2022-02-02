package adudecalledleo.tbsquared.scene;

import java.awt.image.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.text.Text;

public interface SceneRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();

    BufferedImage renderScene(Text text, Map<FacePosition, Face> faces, DataTracker metadata);

    default BufferedImage renderScene(Text text, Map<FacePosition, Face> faces) {
        return renderScene(text, faces, DataTracker.empty());
    }
}
