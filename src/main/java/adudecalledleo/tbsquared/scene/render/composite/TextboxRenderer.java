package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.metadata.MetadataTracker;

@FunctionalInterface
public interface TextboxRenderer {
    void renderTextbox(Graphics2D g, MetadataTracker sceneMeta, int x, int y, int width, int height);
}
