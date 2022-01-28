package adudecalledleo.tbsquared.render.composite.impl;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.render.composite.TextboxRenderer;

public record SimpleTextboxRenderer(BufferedImage textboxImage) implements TextboxRenderer {
    @Override
    public void renderTextbox(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(textboxImage, x, y, null);
    }
}
