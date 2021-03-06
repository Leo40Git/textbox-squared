package adudecalledleo.tbsquared.rpgmaker.test;

import java.awt.*;
import java.awt.image.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import adudecalledleo.tbsquared.icon.DefaultIconPool;
import adudecalledleo.tbsquared.icon.Icon;
import adudecalledleo.tbsquared.icon.IconPool;
import adudecalledleo.tbsquared.parse.DOMConverter;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;
import adudecalledleo.tbsquared.parse.node.color.ColorParser;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowSkin;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowTint;
import adudecalledleo.tbsquared.scene.SceneMetadata;
import adudecalledleo.tbsquared.scene.composite.SolidColorImageFactory;
import adudecalledleo.tbsquared.text.Text;

public final class RPGMakerExtensionTest {
    private RPGMakerExtensionTest() { }

    public static void main(String[] args) {
        final String windowImagePath = "/window.png";
        final String merciaImagePath = "/mercia.png";
        final String iconImagePath = "/icon.png";
        final String fontPath = "/font/VL-Gothic-Regular.ttf";
        final Path outputPath = Paths.get(".", "output.png").toAbsolutePath();

        BufferedImage windowImage, merciaImage, iconImage;
        Font font;

        try {
            windowImage = loadImage(windowImagePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(windowImagePath), e);
        }
        try {
            merciaImage = loadImage(merciaImagePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(merciaImagePath), e);
        }
        try {
            iconImage = loadImage(iconImagePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(iconImagePath), e);
        }

        try (var in = openResourceAsStream(fontPath)) {
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load font from \"%s\"".formatted(fontPath), e);
        } catch (FontFormatException e) {
            throw new RuntimeException("Failed to load font from \"%s\"".formatted(fontPath), e);
        }

        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        font = font.deriveFont(Font.PLAIN, 28);

        Face merciaFace = new Face(Definition.builtin(), "Mercia", merciaImage, (name, image) -> new ImageIcon());

        var icons = DefaultIconPool.builder(32)
                .addIcon(new Icon(Definition.builtin(), "test", iconImage))
                .build();

        RPGWindowSkin winSkin = new RPGWindowSkin(RPGWindowSkin.Version.MV, windowImage,
                new RPGWindowTint(-17, -255, -255),
                0);

        var sceneRenderer = winSkin.sceneRendererBuilder()
                .sceneSize(816, 180)
                .textboxRect(0, 0, 816, 180)
                .imageFactory(new SolidColorImageFactory(Color.BLACK))
                .fonts(SingleFontProvider.of(font, FontMetadata.builder(Definition.builtin()).build()))
                .build();

        var pal = winSkin.getPalette();

        var ctx = DataTracker.builder()
                .set(ColorParser.PALETTE, pal)
                .set(IconPool.ICONS, icons)
                .set(SceneMetadata.ARROW_FRAME, 1)
                .build();

        var result = DOMParser.parse(NodeRegistry.getDefault(),
                """
                Mercia:
                [color=palette(25)]Hold on.
                [i]What?[/i][/color] [icon=test]\\u0123[/icon]
                [style size=-4 color=palette(1)]a[/style]a[style size=+4]a[/style] [sup]b[/sup]b[sub]b[/sub]
                """, ctx, new DOMParser.SpanTracker() {
                    @Override
                    public void markEscaped(int start, int end) {
                        System.out.format("ESCAPE - %d to %d%n", start, end);
                    }

                    @Override
                    public void markNodeDeclOpening(String node, int start, int end) {
                        System.out.format("OPENING NODE DECL - node %s, %d to %d%n", node, start, end);
                    }

                    @Override
                    public void markNodeDeclClosing(String node, int start, int end) {
                        System.out.format("CLOSING NODE DECL - node %s, %d to %d%n", node, start, end);
                    }

                    @Override
                    public void markNodeDeclAttribute(String node, String key, int keyStart, int keyEnd, String value, int valueStart, int valueEnd) {
                        System.out.format("NODE ATTR - node %s, %s (%d to %d) = %s (%d to %d)%n",
                                node, key, keyStart, keyEnd, value, valueStart, valueEnd);
                    }
                }
        );

        if (result.hasErrors()) {
            System.out.format("%d error(s) while parsing:%n", result.errors().size());
            for (var error : result.errors()) {
                System.out.format(" - %s (%d to %d)%n", error.message(), error.start(), error.end());
            }
            return;
        }
        var doc = result.document();

        Text text = DOMConverter.toText(doc, NodeRegistry.getDefault(),
                ctx);

        var image = sceneRenderer.renderScene(text,
                Map.of(sceneRenderer.getDefaultFacePosition(), merciaFace),
                ctx);

        try (var out = Files.newOutputStream(outputPath)) {
            ImageIO.write(image, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write output image to \"%s\"".formatted(outputPath),
                    e);
        }
    }

    private static InputStream openResourceAsStream(String path) throws IOException {
        var in = RPGMakerExtensionTest.class.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(path);
        }
        return in;
    }

    private static BufferedImage loadImage(String path) throws IOException {
        try (var in = openResourceAsStream(path)) {
            return ImageIO.read(in);
        }
    }
}
