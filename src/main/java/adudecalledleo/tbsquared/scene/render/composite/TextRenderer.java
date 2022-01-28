package adudecalledleo.tbsquared.scene.render.composite;

import java.awt.*;

import adudecalledleo.tbsquared.metadata.MetadataTracker;
import adudecalledleo.tbsquared.text.node.NodeList;

public interface TextRenderer {
    void renderText(Graphics2D g, NodeList nodes, MetadataTracker sceneMeta, int x, int y);
}
