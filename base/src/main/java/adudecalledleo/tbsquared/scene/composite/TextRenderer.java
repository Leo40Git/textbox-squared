package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.text.Text;

public interface TextRenderer {
    void renderText(Graphics2D g, Text text, FontProvider fonts, DataTracker sceneMeta, int x, int y);
}
