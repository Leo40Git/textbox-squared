package adudecalledleo.tbsquared.scene.render.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.metadata.MetadataTracker;
import adudecalledleo.tbsquared.scene.render.composite.TextboxRenderer;
import adudecalledleo.tbsquared.util.render.NinePatch;

public final class NinePatchTextboxRenderer implements TextboxRenderer {
    private final NinePatch delegate;

    public NinePatchTextboxRenderer(BufferedImage sourceImage) {
        delegate = new NinePatch(sourceImage);
    }

    @Override
    public void renderTextbox(Graphics2D g, MetadataTracker sceneMeta, int x, int y, int width, int height) {
        delegate.render(g, x, y, width, height);
    }
}
