package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.scene.composite.AbstractTextRenderer;
import adudecalledleo.tbsquared.util.render.GraphicsState;

final class RPGTextRenderer extends AbstractTextRenderer {
    public static final Color OUTLINE_COLOR = new Color(0, 0, 0, 127);
    public static final int OUTLINE_WIDTH = 4;
    private static final Stroke OUTLINE_STROKE = new BasicStroke(OUTLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    // this secondary outline is drawn with the actual text color, to try and replicate RPG Maker MV (AKA browser) AA
    public static final int OUTLINE2_WIDTH = 1;
    public static final float OUTLINE2_OPAQUENESS = 0.275f;
    private static final Stroke OUTLINE2_STROKE = new BasicStroke(OUTLINE2_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
    public static final Composite OUTLINE2_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, OUTLINE2_OPAQUENESS);

    private final Color defaultColor;
    private final int flags;
    private final AffineTransform tx, tx2;

    public RPGTextRenderer(Color defaultColor, int flags) {
        this.defaultColor = defaultColor;
        this.flags = flags;
        tx = new AffineTransform();
        tx2 = new AffineTransform();
    }

    @Override
    protected void setupGraphicsState(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    @Override
    protected void onFontChanged(Graphics2D g, String fontKey, FontMetadata fontMetadata, FontStyle fontStyle) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                fontMetadata.shouldRenderWithAntialiasing()
                        ? RenderingHints.VALUE_ANTIALIAS_ON
                        : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                fontMetadata.shouldRenderWithAntialiasing()
                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    @Override
    protected Color getDefaultTextColor(Graphics2D g, GraphicsState oldState, DataTracker sceneMeta) {
        return defaultColor;
    }

    @Override
    protected int renderTextImpl(Graphics2D g, GraphicsState oldState, String string, DataTracker sceneMeta, int defaultMaxAscent, int x, int y) {
        // fudge Y a bit
        y += 3;
        // make the text vertically centered
        final int yo = defaultMaxAscent / 2 - g.getFontMetrics().getMaxAscent() / 2;

        // generate an outline of the text
        var layout = new TextLayout(string, g.getFont(), g.getFontRenderContext());

        boolean flipH = false, flipV = false; // TODO maybe reimpl gimmicks?

        Shape outline;

        if (flipH || flipV) {
            var bounds = layout.getBounds();
            double moveX = x;
            if (flipH) {
                moveX += bounds.getWidth();
            }
            double moveY = y + defaultMaxAscent - yo;
            if (flipV) {
                moveY -= bounds.getHeight();
            }
            tx.setToScale(flipH ? -1 : 1, flipV ? -1 : 1);
            tx2.setToTranslation(moveX, moveY);
            outline = layout.getOutline(tx);
            outline = tx2.createTransformedShape(outline);
        } else {
            tx.setToTranslation(x, y + defaultMaxAscent - yo);
            outline = layout.getOutline(tx);
        }

        var c = g.getColor(); // (save the actual text color for later)
        if ((flags & RPGWindowSkin.TEXT_NO_OUTLINE) == 0) {
            // draw a transparent outline of the text...
            g.setStroke(OUTLINE_STROKE);
            g.setColor(OUTLINE_COLOR);
            g.draw(outline);
            g.setColor(c);
            // ...then draw a secondary outline...
            g.setComposite(OUTLINE2_COMPOSITE);
            g.setStroke(OUTLINE2_STROKE);
            g.draw(outline);
        }
        // ...and then, fill in the text!
        g.setComposite(oldState.composite());
        g.fill(outline);
        g.setColor(c);

        return (int) layout.getAdvance();
    }

    // standard RPG Maker doesn't support icons, but w/e
    @Override
    protected int renderIconImpl(Graphics2D g, GraphicsState oldState, int iconSize, BufferedImage icon, DataTracker sceneMeta, int defaultMaxAscent, int x, int y) {
        // fudge Y a bit
        y += 3;
        // make the text vertically centered
        y += defaultMaxAscent / 2 - iconSize / 2;

        g.drawImage(icon, x, y, null);

        return iconSize + 2;
    }

    @Override
    protected int calculateLineAdvance(int maxAscent) {
        return 36;
    }
}
