package adudecalledleo.tbsquared.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.text.node.NodeList;

public interface TextRenderer {
    void renderText(Graphics2D g, NodeList nodes, int x, int y);
}
