package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.util.shape.Rect;

public final class RPGBackgroundRenderer {
    private static final class BackImageTL extends InheritableThreadLocal<BufferedImage> {
        @Override
        protected BufferedImage childValue(BufferedImage parentValue) {
            if (parentValue == null) {
                return null;
            } else {
                return new BufferedImage(parentValue.getColorModel(),
                        parentValue.copyData(parentValue.getRaster().createCompatibleWritableRaster()),
                        parentValue.isAlphaPremultiplied(), null);
            }
        }
    }

    private final BufferedImage windowImage;
    private final RPGWindowTint backTint;

    private final int backMargin;
    private final int backTileSize;
    private final Rect backBase, backOverlay;
    private final ThreadLocal<BufferedImage> backImage;

    RPGBackgroundRenderer(RPGWindowSkin.Version version, BufferedImage windowImage, RPGWindowTint backTint) {
        this.windowImage = windowImage;

        this.backTint = backTint;
        backMargin = version.textboxMargin();
        backTileSize = version.scale(64);
        backBase = new Rect(0, 0, backTileSize, backTileSize);
        backOverlay = new Rect(0, backTileSize, backTileSize, backTileSize);
        backImage = new BackImageTL();
    }

    public void renderBackground(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(getBackImage(width - backMargin, height - backMargin),
                x + backMargin, y + backMargin, x + width - backMargin, y + height - backMargin,
                0, 0, width - backMargin, height - backMargin,
                null);
    }

    private BufferedImage getBackImage(int width, int height) {
        BufferedImage backImage = this.backImage.get();
        if (backImage == null || backImage.getWidth() != width || backImage.getHeight() != height) {
            backImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            var g = backImage.createGraphics();

            // draw stretched and tinted base
            g.setComposite(new RPGWindowTintComposite(backTint));
            g.drawImage(windowImage,
                    0, 0, width, height,
                    backBase.x1(), backBase.y1(), backBase.x2(), backBase.y2(),
                    null);
            // draw tiled overlay
            g.setComposite(AlphaComposite.SrcOver);
            final int tilesWide = width / backTileSize, tilesHigh = height / backTileSize;
            for (int ty = 0; ty <= tilesHigh; ty++) {
                for (int tx = 0; tx <= tilesWide; tx++) {
                    g.drawImage(windowImage,
                            tx * backTileSize, ty * backTileSize,
                            tx * backTileSize + backOverlay.width(), ty * backTileSize + backOverlay.height(),
                            backOverlay.x1(), backOverlay.y1(), backOverlay.x2(), backOverlay.y2(),
                            null);
                }
            }
            g.dispose();

            // reduce everyone's alpha by 25%
            // NOTE: this loop relies on the BG image being of TYPE_INT_ARGB!
            int[] pixels = ((DataBufferInt) backImage.getRaster().getDataBuffer()).getData();
            for (int i = 0; i < pixels.length; i++) {
                // mask out old alpha and OR in new alpha
                pixels[i] = (pixels[i] & ~0xFF000000) | (((int) Math.floor(((pixels[i] >> 24) & 0xFF) * 0.75)) << 24);
            }

            this.backImage.set(backImage);
        }
        return backImage;
    }

}
