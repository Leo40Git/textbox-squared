package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.util.Optional;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextStyle;
import adudecalledleo.tbsquared.text.TextVisitor;
import adudecalledleo.tbsquared.util.Unit;
import adudecalledleo.tbsquared.util.render.GraphicsState;

public abstract class AbstractTextRenderer implements TextRenderer, TextVisitor<AbstractTextRenderer.RendererState, Unit> {
    protected static final class RendererState {
        public final Graphics2D graphics;
        public final DataTracker sceneMeta;
        public final FontProvider fonts;
        public final GraphicsState oldState;
        public final int defaultMaxAscent;
        public final String defaultFontKey;
        public final int startX;
        public final StringBuilder stringBuilder;

        public int x, y;
        public String fontKey;
        public FontMetadata fontMetadata;
        public FontStyle fontStyle;

        public RendererState(Graphics2D graphics, DataTracker sceneMeta, FontProvider fonts, GraphicsState oldState,
                             int defaultMaxAscent, String defaultFontKey, int startX, int startY) {
            this.graphics = graphics;
            this.sceneMeta = sceneMeta;
            this.fonts = fonts;
            this.oldState = oldState;
            this.defaultMaxAscent = defaultMaxAscent;
            this.defaultFontKey = defaultFontKey;
            this.startX = x = startX;
            this.y = startY;
            this.stringBuilder = new StringBuilder();
        }

        public void resetX() {
            this.x = startX;
        }

        public void setFont(String key) {
            this.fontKey = key;
            this.fontMetadata = fonts.getFontMetadata(key);
        }
    }

    @Override
    public final void renderText(Graphics2D g, Text text, FontProvider fonts, DataTracker sceneMeta, int x, int y) {
        var oldState = GraphicsState.save(g);
        setupGraphicsState(g);

        String fontKey = fonts.getDefaultFontKey();
        FontMetadata fontMetadata = fonts.getDefaultFontMetadata();
        FontStyle fontStyle = FontStyle.DEFAULT;
        g.setFont(fonts.getStyledFont(fontKey, fontStyle));
        onFontChanged(g, fontKey, fontMetadata, fontStyle);

        final int defaultMaxAscent = g.getFontMetrics().getMaxAscent();

        RendererState state = new RendererState(g, sceneMeta, fonts, oldState, defaultMaxAscent, fontKey, x, y);
        state.fontKey = fontKey;
        state.fontMetadata = fontMetadata;
        state.fontStyle = fontStyle;

        text.visit(this, state);

        oldState.restore(g);
    }

    @Override
    public final Optional<Unit> visit(TextStyle style, String contents, RendererState state) {
        final var g = state.graphics;
        final var oldState = state.oldState;
        final var sceneMeta = state.sceneMeta;
        final int defaultMaxAscent = state.defaultMaxAscent;
        final var sb = state.stringBuilder;

        g.setColor(style.color().orElseGet(() -> getDefaultTextColor(g, oldState, sceneMeta)));

        var newFontKey = style.font().orElse(state.defaultFontKey);
        var newFontStyle = style.toFontStyle();
        if (!newFontKey.equals(state.fontKey) || !newFontStyle.equals(state.fontStyle)) {
            g.setFont(state.fonts.getStyledFont(newFontKey, newFontStyle));
            state.setFont(newFontKey);
            state.fontStyle = newFontStyle;
            onFontChanged(g, newFontKey, state.fontMetadata, newFontStyle);
        }

        for (var c : contents.toCharArray()) {
            if (c == '\n') {
                if (!sb.isEmpty()) {
                    renderTextImpl(g, oldState, sb.toString(), sceneMeta, defaultMaxAscent, state.x, state.y);
                    sb.setLength(0);
                }
                state.resetX();
                state.y += calculateLineAdvance(defaultMaxAscent);
            } else {
                sb.append(c);
            }
        }
        if (!sb.isEmpty()) {
            state.x += renderTextImpl(g, oldState, sb.toString(), sceneMeta, defaultMaxAscent, state.x, state.y);
            sb.setLength(0);
        }
        return Optional.empty();
    }

    protected abstract void setupGraphicsState(Graphics2D g);

    protected void onFontChanged(Graphics2D g, String fontKey, FontMetadata fontMetadata, FontStyle fontStyle) { }

    protected Color getDefaultTextColor(Graphics2D g, GraphicsState oldState, DataTracker sceneMeta) {
        return oldState.foreground();
    }

    /**
     * @return string advance
     */
    protected abstract int renderTextImpl(Graphics2D g, GraphicsState oldState, String string, DataTracker sceneMeta, int defaultMaxAscent, int x, int y);

    protected int calculateLineAdvance(int maxAscent) {
        return maxAscent;
    }
}
