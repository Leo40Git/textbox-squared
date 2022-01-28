package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.node.NodeList;

public interface TextRenderer {
    void renderText(Graphics2D g, NodeList nodes, DataTracker sceneMeta, int x, int y);
}
