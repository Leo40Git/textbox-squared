package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.scene.composite.FaceRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.SingleFacePositionRenderer;

public final class RPGFaceRenderer extends SingleFacePositionRenderer {
    public static final FaceRenderer INSTANCE = new RPGFaceRenderer();

    private RPGFaceRenderer() { }

    @Override
    protected Dimension renderFace(Graphics2D g, Face face, DataTracker sceneMeta, int x, int y) {
        if (face.isBlank()) {
            return new Dimension();
        }
        g.drawImage(face.getImage(), x, y, null);
        return new Dimension(face.getImage().getWidth() + 24, 0);
    }
}
