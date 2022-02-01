package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.scene.composite.FaceRenderer;
import adudecalledleo.tbsquared.scene.composite.SingleFaceRenderer;
import adudecalledleo.tbsquared.util.shape.Dim;

public final class RPGFaceRenderer extends SingleFaceRenderer {
    public static final FaceRenderer INSTANCE = new RPGFaceRenderer();

    private RPGFaceRenderer() { }

    @Override
    protected Dim renderFace(Graphics2D g, Face face, DataTracker sceneMeta, int x, int y) {
        if (face.isBlank()) {
            return Dim.ZERO;
        }
        g.drawImage(face.getImage(), x, y, null);
        return new Dim(face.getImage().getWidth() + 24, 0);
    }
}
