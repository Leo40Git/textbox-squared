package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.text.node.NodeList;

public interface TextRenderer {
    void renderText(Graphics2D g, NodeList nodes, FontProvider fonts, DataTracker sceneMeta, int x, int y);
}
