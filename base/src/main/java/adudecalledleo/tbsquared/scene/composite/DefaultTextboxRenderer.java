package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.data.DataTracker;

public class DefaultTextboxRenderer implements TextboxRenderer {
    protected final BufferedImage backgroundImage;

    public DefaultTextboxRenderer(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    public void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        g.drawImage(backgroundImage, x, y, null);
    }
}
