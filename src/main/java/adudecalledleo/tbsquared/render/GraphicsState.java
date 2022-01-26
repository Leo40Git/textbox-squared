package adudecalledleo.tbsquared.render;

import java.awt.*;
import java.awt.geom.*;

public record GraphicsState(Composite composite, Paint paint, Stroke stroke,
                            RenderingHints renderingHints, AffineTransform transform,
                            Color background, Shape clip) {
    public static GraphicsState save(Graphics2D g2d) {
        return new GraphicsState(g2d.getComposite(), g2d.getPaint(), g2d.getStroke(), g2d.getRenderingHints(),
                g2d.getTransform(), g2d.getBackground(), g2d.getClip());
    }

    public void restore(Graphics2D g2d) {
        g2d.setComposite(composite);
        g2d.setPaint(paint);
        g2d.setStroke(stroke);
        g2d.setRenderingHints(renderingHints);
        g2d.setTransform(transform);
        g2d.setBackground(background);
        g2d.setClip(clip);
    }
}
