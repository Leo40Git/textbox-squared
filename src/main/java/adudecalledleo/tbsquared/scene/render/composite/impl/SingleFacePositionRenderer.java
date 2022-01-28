package adudecalledleo.tbsquared.scene.render.composite.impl;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.render.composite.FaceRenderer;

public abstract class SingleFacePositionRenderer implements FaceRenderer {
    public static final String THE_ONLY_POSITION_NAME = "the_one";
    public static final FacePosition THE_ONLY_POSITION = new FacePosition() {
        @Override
        public String name() {
            return THE_ONLY_POSITION_NAME;
        }

        @Override
        public String toString() {
            return THE_ONLY_POSITION_NAME;
        }
    };
    private static final Collection<? extends FacePosition> FACE_POSITIONS = Set.of(THE_ONLY_POSITION);

    @Override
    public Collection<? extends FacePosition> getFacePositions() {
        return FACE_POSITIONS;
    }

    @Override
    public FacePosition getDefaultFacePosition() {
        return THE_ONLY_POSITION;
    }

    @Override
    public Point renderFaces(Graphics2D g, Map<FacePosition, Face> faces, DataTracker sceneMeta, int x, int y) {
        Face face = faces.get(THE_ONLY_POSITION);
        if (face == null) {
            throw new IllegalStateException("Missing required face in position \"%s\"".formatted(THE_ONLY_POSITION_NAME));
        }
        return renderFace(g, face, sceneMeta, x, y);
    }

    protected abstract Point renderFace(Graphics2D g, Face face, DataTracker sceneMeta, int x, int y);
}
