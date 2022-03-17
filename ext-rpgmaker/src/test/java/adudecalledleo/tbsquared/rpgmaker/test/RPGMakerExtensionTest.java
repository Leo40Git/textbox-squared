package adudecalledleo.tbsquared.rpgmaker.test;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import adudecalledleo.tbsquared.data.DefaultDataTracker;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.SingleFontProvider;
import adudecalledleo.tbsquared.parse.DOMConverter;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;
import adudecalledleo.tbsquared.parse.node.color.ColorSelector;
import adudecalledleo.tbsquared.parse.node.span.NodeDeclarationType;
import adudecalledleo.tbsquared.parse.node.span.NodeSpanTracker;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowSkin;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowTint;
import adudecalledleo.tbsquared.scene.SceneMetadata;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.scene.composite.SolidColorImageFactory;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.util.resource.AWTResourceLoader;

public final class RPGMakerExtensionTest {
    private RPGMakerExtensionTest() { }

    public static void main(String[] args) {
        final String windowImagePath = "/window.png";
        final String merciaImagePath = "/mercia.png";
        final String fontPath = "/font/VL-Gothic-Regular.ttf";
        final Path outputPath = Paths.get(".", "output.png").toAbsolutePath();

        BufferedImage windowImage, merciaImage;
        Font font;

        var resources = new AWTResourceLoader(RPGMakerExtensionTest.class);

        try {
            windowImage = resources.loadImage(windowImagePath);
        } catch (IOException e2) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(windowImagePath), e2);
        }
        try {
            merciaImage = resources.loadImage(merciaImagePath);
        } catch (IOException e1) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(merciaImagePath), e1);
        }

        try {
            font = resources.loadFont(Font.TRUETYPE_FONT, fontPath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load font from \"%s\"".formatted(fontPath), e);
        } catch (FontFormatException e) {
            throw new RuntimeException("Failed to load font from \"%s\"".formatted(fontPath), e);
        }

        font = font.deriveFont(Font.PLAIN, 28);

        Face merciaFace = new Face(Definition.builtin(), "Mercia", merciaImage, (name, image) -> new ImageIcon());

        RPGWindowSkin winSkin = new RPGWindowSkin(RPGWindowSkin.Version.MV, windowImage,
                new RPGWindowTint(-17, -255, -255),
                0);

        SceneRenderer sceneRenderer = winSkin.sceneRendererBuilder()
                .sceneSize(816, 180)
                .textboxRect(0, 0, 816, 180)
                .imageFactory(new SolidColorImageFactory(Color.BLACK))
                .fonts(SingleFontProvider.of(font, FontMetadata.builder(Definition.builtin()).build()))
                .build();

        var pal = winSkin.getPalette();

        var doc = DOMParser.parse(NodeRegistry.getDefault(),
                new NodeSpanTracker() {
                    @Override
                    public void markEscape(int start, int end) {
                        System.out.format("ESCAPE - %d to %d%n", start, end);
                    }

                    @Override
                    public void markNodeDeclaration(String node, NodeDeclarationType type, int start, int end) {
                        System.out.format("%s NODE DECL - node %s, %d to %d%n", type.toString(), node, start, end);
                    }

                    @Override
                    public void markNodeAttribute(String node, String key, int keyStart, int keyEnd, String value, int valueStart, int valueEnd) {
                        System.out.format("NODE ATTR - node %s, %s (%d to %d) = %s (%d to %d)%n",
                                node, key, keyStart, keyEnd, value, valueStart, valueEnd);
                    }
                },
                        """
                        Mercia:
                        [color=palette(25)]Hold on.
                        [i]What?[/i][/color] \\u0123
                        [style size=-4 color=palette(1)]a[/style]a[style size=+4]a[/style] [sup]b[/sup]b[sub]b[/sub]
                        """);

        Text text = DOMConverter.toText(doc, NodeRegistry.getDefault(),
                DefaultDataTracker.of(ColorSelector.PALETTE, pal));

        var image = sceneRenderer.renderScene(text,
                Map.of(sceneRenderer.getDefaultFacePosition(), merciaFace),
                DefaultDataTracker.of(SceneMetadata.ARROW_FRAME, 1));

        try (var out = Files.newOutputStream(outputPath)) {
            ImageIO.write(image, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write output image to \"%s\"".formatted(outputPath),
                    e);
        }
    }
}
