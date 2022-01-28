package adudecalledleo.tbsquared.scene.render.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.metadata.MetadataTracker;
import adudecalledleo.tbsquared.scene.render.composite.TextboxRenderer;

public record DefaultTextboxRenderer(BufferedImage textboxImage) implements TextboxRenderer {
    @Override
    public void renderTextbox(Graphics2D g, MetadataTracker sceneMeta, int x, int y, int width, int height) {
        g.drawImage(textboxImage, x, y, null);
    }
}
