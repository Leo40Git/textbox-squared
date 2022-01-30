package adudecalledleo.tbsquared.test;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.SingleFontProvider;
import adudecalledleo.tbsquared.scene.Scene;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.scene.composite.CompositeSceneRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.AbstractTextRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.DefaultSceneImageFactory;
import adudecalledleo.tbsquared.scene.composite.impl.NinePatchTextboxRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.SingleFacePositionRenderer;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.TextStyle;
import adudecalledleo.tbsquared.util.Resources;
import adudecalledleo.tbsquared.util.render.Colors;
import adudecalledleo.tbsquared.util.render.GraphicsState;

public final class CompositeScreenRendererTest {
    private CompositeScreenRendererTest() { }

    private static final class TextRendererImpl extends AbstractTextRenderer {
        private final AffineTransform tx;

        public TextRendererImpl() {
            tx = new AffineTransform();
        }

        @Override
        protected void setupGraphicsState(Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }

        @Override
        protected int renderString(Graphics2D g, GraphicsState oldState, String string, DataTracker sceneMeta, int defaultMaxAscent, int x, int y) {
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

    private static final class FaceRendererImpl extends SingleFacePositionRenderer {
        @Override
        protected Dimension renderFace(Graphics2D g, Face face, DataTracker sceneMeta, int x, int y) {
            if (face.isBlank()) {
                return new Dimension(0, 0);
            }
            g.drawImage(face.getImage(), x, y, null);
            return new Dimension(144 + 12, 0);
        }
    }

    public static void main(String[] args) {
        String tbSourcePath = "/ninepatch.png";
        String faceSourcePath = "/face.png";
        Path outputPath = Paths.get(".", "output.png").toAbsolutePath();

        final int sW = 800, sH = 600;
        final int tbW = sW, tbH = 168;

        Font font = new Font("Arial", Font.PLAIN, 21);

        BufferedImage tbSourceImage;
        try (var in = Resources.openStream(CompositeScreenRendererTest.class, tbSourcePath)) {
            tbSourceImage = ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read textbox source image from \"%s\"".formatted(tbSourcePath),
                    e);
        }

        BufferedImage faceImage;
        try (var in = Resources.openStream(CompositeScreenRendererTest.class, faceSourcePath)) {
            faceImage = ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read face image from \"%s\"".formatted(faceSourcePath),
                    e);
        }

        Face face = new Face(Definition.builtin(), "Test", faceImage, (name, image) -> new ImageIcon(image, name));

        Text text = new TextBuilder()
                .style(TextStyle.EMPTY.withColor(Color.WHITE))
                .append("Testing!")
                .newLine()
                .append("Colors are ")
                .style(TextStyle.EMPTY.withColor(Colors.brighter(Color.BLUE, 0.7)))
                .append("cool!")
                .style(TextStyle.EMPTY.withColor(Color.WHITE))
                .newLine()
                .append("Get ")
                .style(TextStyle.EMPTY.withBold(true))
                .append("bold!")
                .build();

        SceneRenderer sceneRenderer = new CompositeSceneRenderer(
                new CompositeSceneRenderer.Config(
                        new Dimension(sW, sH),
                        Colors.darker(Color.RED, 0.2),
                        new Rectangle(0, sH - tbH, tbW, tbH),
                        new Dimension(12, 12)
                ),
                DefaultSceneImageFactory.INSTANCE,
                SingleFontProvider.of(font, FontMetadata.builder(Definition.builtin()).build()),
                new NinePatchTextboxRenderer(tbSourceImage),
                new FaceRendererImpl(),
                new TextRendererImpl()
        );

        Scene scene = new Scene(text, Map.of(SingleFacePositionRenderer.THE_ONLY_POSITION, face));

        var image = sceneRenderer.renderScene(scene);

        try (var out = Files.newOutputStream(outputPath)) {
            ImageIO.write(image, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write output image to \"%s\"".formatted(outputPath),
                    e);
        }
    }
}
