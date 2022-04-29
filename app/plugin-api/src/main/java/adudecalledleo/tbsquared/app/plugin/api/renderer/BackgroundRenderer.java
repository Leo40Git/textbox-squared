package adudecalledleo.tbsquared.app.plugin.api.renderer;

import java.awt.*;

@FunctionalInterface
public interface BackgroundRenderer {
    void renderBackground(Graphics2D g, int x, int y, int width, int height);
}
