package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;

@FunctionalInterface
public interface TextboxRenderer {
    void renderTextbox(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height);
}
