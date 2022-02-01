package adudecalledleo.tbsquared.scene.composite.impl;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;

public record DualTextboxRenderer(TextboxRenderer background, int backgroundMargin,
                                  TextboxRenderer foreground) implements TextboxRenderer {
    @Override
    public void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        background.renderBackground(g, sceneMeta,
                x + backgroundMargin, y + backgroundMargin,
                width - backgroundMargin, height - backgroundMargin);
    }

    @Override
    public void renderForeground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        foreground.renderBackground(g, sceneMeta, x, y, width, height);
    }
}
