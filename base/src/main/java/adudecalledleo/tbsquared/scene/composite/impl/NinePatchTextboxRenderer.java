package adudecalledleo.tbsquared.scene.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import adudecalledleo.tbsquared.util.render.NinePatch;

public class NinePatchTextboxRenderer implements TextboxRenderer {
    protected final NinePatch delegate;

    public NinePatchTextboxRenderer(BufferedImage sourceImage) {
        delegate = new NinePatch(sourceImage);
    }

    public NinePatch getDelegate() {
        return delegate;
    }

    @Override
    public void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        delegate.render(g, x, y, width, height);
    }
}
