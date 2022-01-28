package adudecalledleo.tbsquared.scene.render.composite.impl;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.scene.render.composite.TextRenderer;
import adudecalledleo.tbsquared.text.modifier.color.ColorModifierNode;
import adudecalledleo.tbsquared.text.modifier.style.StyleModifierNode;
import adudecalledleo.tbsquared.text.modifier.style.StyleSpec;
import adudecalledleo.tbsquared.text.node.LineBreakNode;
import adudecalledleo.tbsquared.text.node.NodeList;
import adudecalledleo.tbsquared.text.node.TextNode;
import adudecalledleo.tbsquared.util.render.GraphicsState;

public abstract class AbstractTextRenderer implements TextRenderer {
    @Override
    public final void renderText(Graphics2D g, NodeList nodes, FontProvider fonts, DataTracker sceneMeta, int x, int y) {
        var oldState = GraphicsState.save(g);
        setupGraphicsState(g);

        String fontKey = fonts.getDefaultFontKey();
        FontMetadata fontMetadata = fonts.getDefaultFontMetadata();
        FontStyle fontStyle = FontStyle.DEFAULT;
        g.setFont(fonts.getStyledFont(fontKey, fontStyle));
        onFontChanged(g, fontKey, fontMetadata, fontStyle);

        final int ma = g.getFontMetrics().getMaxAscent();
        final int startX = x;
        StyleSpec modStyleSpec = StyleSpec.DEFAULT;

        for (var node : nodes) {
            if (node instanceof TextNode text) {
                if (text.getContents().isEmpty()) {
                    continue;
                }

                // make the text vertically centered
                int yo = ma / 2 - g.getFontMetrics().getMaxAscent() / 2;

                x += renderString(g, oldState, text.getContents(), sceneMeta, x, y + ma - yo);
            } else if (node instanceof ColorModifierNode modColor) {
                g.setColor(modColor.getColor());
            } else if (node instanceof StyleModifierNode modStyle) {
                modStyleSpec = modStyleSpec.add(modStyle.getSpec());
                var newFontStyle = modStyleSpec.toFontStyle();
                if (!fontStyle.equals(newFontStyle)) {
                    fontStyle = newFontStyle;
                    g.setFont(fonts.getStyledFont(fontKey, fontStyle));
                    onFontChanged(g, fontKey, fontMetadata, fontStyle);
                }
            } else if (node instanceof LineBreakNode) {
                x = startX;
                y += 36;
            }
        }

        oldState.restore(g);
    }

    protected abstract void setupGraphicsState(Graphics2D g);
    protected abstract void onFontChanged(Graphics2D g, String fontKey, FontMetadata fontMetadata, FontStyle fontStyle);
    /**
     * @return string advance
     */
    protected abstract int renderString(Graphics2D g, GraphicsState oldState, String string, DataTracker sceneMeta, int x, int y);
}
