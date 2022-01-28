package adudecalledleo.tbsquared.scene.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;

public record DefaultTextboxRenderer(BufferedImage textboxImage) implements TextboxRenderer {
    @Override
    public void renderTextbox(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        g.drawImage(textboxImage, x, y, null);
    }
}
