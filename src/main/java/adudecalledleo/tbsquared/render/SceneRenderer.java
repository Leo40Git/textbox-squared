package adudecalledleo.tbsquared.render;

import java.awt.image.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.text.node.NodeList;

public interface SceneRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();

    BufferedImage renderScene(Map<FacePosition, Face> faces, NodeList nodes);

    default BufferedImage renderScene(Face face, NodeList nodes) {
        return renderScene(Map.of(getDefaultFacePosition(), face), nodes);
    }
}
