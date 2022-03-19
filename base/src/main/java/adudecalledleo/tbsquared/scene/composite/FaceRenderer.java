package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.util.shape.Dim;

public interface FaceRenderer {
    FacePosition getDefaultFacePosition();
    /**
     * @return the X and Y offset the textbox's text should be rendered with
     */
    Dim renderFaces(Graphics2D g, Map<FacePosition, Face> faces, DataTracker sceneMeta, int x, int y);
}
