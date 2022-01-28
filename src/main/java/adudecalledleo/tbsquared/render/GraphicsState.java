package adudecalledleo.tbsquared.render;

import java.awt.*;
import java.awt.geom.*;

public record GraphicsState(Composite composite, Paint paint, Stroke stroke,
                            RenderingHints renderingHints, AffineTransform transform,
                            Color background, Shape clip, Font font) {
    public static GraphicsState save(Graphics2D g) {
        return new GraphicsState(g.getComposite(), g.getPaint(), g.getStroke(), g.getRenderingHints(),
                g.getTransform(), g.getBackground(), g.getClip(), g.getFont());
    }

    public void restore(Graphics2D g) {
        g.setComposite(composite);
        g.setPaint(paint);
        g.setStroke(stroke);
        g.setRenderingHints(renderingHints);
        g.setTransform(transform);
        g.setBackground(background);
        g.setClip(clip);
        g.setFont(font);
    }
}
