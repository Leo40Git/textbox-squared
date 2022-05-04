package adudecalledleo.tbsquared.app.test;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

import adudecalledleo.tbsquared.app.plugin.api.config.ConfigSpec;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SolidColorBackgroundRenderer;
import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.data.DataTrackerBuilder;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.FacePool;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.font.SingleFontProvider;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.scene.composite.*;
import adudecalledleo.tbsquared.util.render.GraphicsState;
import adudecalledleo.tbsquared.util.shape.Dim;

public class TestSceneRendererProvider implements SceneRendererProvider {
    private static final class TextboxRendererImpl implements TextboxRenderer {
        @Override
        public void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }

        @Override
        public void renderForeground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
            g.setColor(Color.WHITE);
            g.drawRect(x, y, width, height);
            g.drawRect(x + 1, y + 1, width - 1, height - 1);
        }
    }

    private static final class TextRendererImpl extends AbstractTextRenderer {
        private final AffineTransform tx;

        public TextRendererImpl() {
            tx = new AffineTransform();
        }

        @Override
        protected void setupGraphicsState(Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }

        @Override
        protected int renderTextImpl(Graphics2D g, GraphicsState oldState, String string, DataTracker sceneMeta, int defaultMaxAscent, int x, int y) {
            // make the text vertically centered
            int yo = defaultMaxAscent / 2 - g.getFontMetrics().getMaxAscent() / 2;

            tx.setToTranslation(x, y + defaultMaxAscent - yo);

            var layout = new TextLayout(string, g.getFont(), g.getFontRenderContext());
            var outline = layout.getOutline(tx);

            g.fill(outline);

            return (int) layout.getAdvance();
        }

        @Override
        protected int calculateLineAdvance(int maxAscent) {
            return maxAscent + 4;
        }
    }

    private static final class FaceRendererImpl extends SingleFaceRenderer {
        @Override
        protected Dim renderFace(Graphics2D g, Face face, DataTracker sceneMeta, int x, int y) {
            if (face.isBlank()) {
                return Dim.ZERO;
            }
            g.drawImage(face.getImage(), x, y, null);
            return new Dim(144 + 12, 0);
        }
    }

    //private static final BackgroundRenderer BG_RENDERER = new SolidColorBackgroundRenderer(Color.BLACK);
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 20);
    private static final FontProvider FONT_PROVIDER = SingleFontProvider.of(FONT,
            FontMetadata.builder(Definition.builtin())
                    .renderWithAntialiasing(false)
                    .build());

    private static final SceneRenderer SCENE_RENDERER = CompositeSceneRenderer.builder()
            .sceneSize(800, 168)
            .textboxRect(0, 0, 800, 168)
            .textboxPadding(4, 4)
            .imageFactory(ImageFactory.BLANK)
            .fonts(FONT_PROVIDER)
            .textboxRenderer(new TextboxRendererImpl())
            .textRenderer(new TextRendererImpl())
            .faceRenderer(new FaceRendererImpl())
            .build();

    private static final DataTracker METADATA = new DataTrackerBuilder()
            .set(FACES, FacePool.empty())
            .set(BACKGROUND_RENDERER, new SolidColorBackgroundRenderer(Color.BLACK))
            .set(FONTS, FONT_PROVIDER)
            .set(TEXT_COLOR, Color.WHITE)
            .build();

    public static final SceneRendererProvider INSTANCE = new TestSceneRendererProvider();

    @Override
    public String getName() {
        return "TEST RENDERER!";
    }

    @Override
    public ConfigSpec getConfigSpec() {
        return ConfigSpec.EMPTY;
    }

    @Override
    public SceneRenderer createSceneRenderer(DataTracker config) {
        return SCENE_RENDERER;
    }

    @Override
    public DataTracker getMetadata() {
        return METADATA;
    }
}
