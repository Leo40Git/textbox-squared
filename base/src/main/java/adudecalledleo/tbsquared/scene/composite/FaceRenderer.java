package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.scene.FacePosition;

public interface FaceRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();
    /**
     * @return the padding of the textbox's text
     */
    Dimension renderFaces(Graphics2D g, Map<FacePosition, Face> faces, DataTracker sceneMeta, int x, int y);
}
