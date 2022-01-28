package adudecalledleo.tbsquared.render.composite;

import java.awt.*;

@FunctionalInterface
public interface TextboxRenderer {
    void renderTextbox(Graphics2D g, int x, int y, int width, int height);
}
