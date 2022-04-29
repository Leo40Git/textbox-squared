package adudecalledleo.tbsquared.app.plugin.api.renderer;

import java.awt.*;

public record SolidColorBackgroundRenderer(Color color) implements BackgroundRenderer {
    @Override
    public void renderBackground(Graphics2D g, int x, int y, int width, int height) {
        var c = g.getColor();
        g.setColor(this.color);
        g.fillRect(x, y, width, height);
        g.setColor(c);
    }
}
