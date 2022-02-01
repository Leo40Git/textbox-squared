package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;

@FunctionalInterface
public interface TextboxRenderer {
    void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height);

    default void renderForeground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {

    }
}
