package adudecalledleo.tbsquared.render.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.render.composite.TextboxRenderer;
import adudecalledleo.tbsquared.render.util.NinePatch;

public final class NinePatchTextboxRenderer implements TextboxRenderer {
    private final NinePatch delegate;

    public NinePatchTextboxRenderer(BufferedImage sourceImage) {
        delegate = new NinePatch(sourceImage);
    }

    @Override
    public void renderTextbox(Graphics2D g, int x, int y, int width, int height) {
        delegate.render(g, x, y, width, height);
    }
}
