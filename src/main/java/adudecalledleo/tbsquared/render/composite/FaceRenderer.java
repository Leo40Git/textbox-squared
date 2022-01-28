package adudecalledleo.tbsquared.render.composite;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.render.FacePosition;

public interface FaceRenderer {
    Collection<? extends FacePosition> getFacePositions();
    FacePosition getDefaultFacePosition();
    /**
     * @return the starting position offset of the textbox's text
     */
    Point renderFaces(Graphics2D g, Map<FacePosition, Face> faces, int x, int y);
}
