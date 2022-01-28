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

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.data.DefaultDataTracker;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.font.FontMetadata;
import adudecalledleo.tbsquared.font.SingleFontProvider;
import adudecalledleo.tbsquared.rpgmaker.RPGFaceRenderer;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowSkin;
import adudecalledleo.tbsquared.rpgmaker.RPGWindowTint;
import adudecalledleo.tbsquared.scene.Scene;
import adudecalledleo.tbsquared.scene.SceneMetadata;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.scene.composite.CompositeSceneRenderer;
import adudecalledleo.tbsquared.scene.composite.FaceRenderer;
import adudecalledleo.tbsquared.scene.composite.impl.DefaultSceneImageFactory;
import adudecalledleo.tbsquared.text.TextParser;
import adudecalledleo.tbsquared.text.modifier.ModifierRegistry;
import adudecalledleo.tbsquared.text.modifier.color.ColorModifierNode;
import adudecalledleo.tbsquared.text.node.NodeList;
import adudecalledleo.tbsquared.util.Resources;

public final class RPGMakerExtensionTest {
    private RPGMakerExtensionTest() { }

    public static void main(String[] args) {
        final String fontPath = "/font/VL-Gothic-Regular.ttf";
        final Path outputPath = Paths.get(".", "output.png").toAbsolutePath();

        BufferedImage windowImage, merciaImage;
        Font font;

        windowImage = loadImage("/window.png");
        merciaImage = loadImage("/mercia.png");

        try (var in = Resources.openStream(RPGMakerExtensionTest.class, fontPath)) {
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load font from \"%s\"".formatted(fontPath), e);
        } catch (FontFormatException e) {
            throw new RuntimeException("Failed to load font from \"%s\"".formatted(fontPath), e);
        }

        font = font.deriveFont(Font.PLAIN, 28);

        Face merciaFace = new Face(Definition.builtin(), "Mercia", merciaImage, (name, image) -> new ImageIcon());

        RPGWindowSkin winSkin = new RPGWindowSkin(RPGWindowSkin.Version.MV, windowImage,
                new RPGWindowTint(-17, -255, -255));
        FaceRenderer faceRenderer = new RPGFaceRenderer();

        SceneRenderer sceneRenderer = new CompositeSceneRenderer(
                new CompositeSceneRenderer.Config(
                        new Dimension(816, 180),
                        Color.BLACK,
                        new Rectangle(0, 0, 816, 180),
                        new Dimension(RPGWindowSkin.Version.MV.textboxPadding(), RPGWindowSkin.Version.MV.textboxPadding())
                ),
                new DefaultSceneImageFactory(),
                SingleFontProvider.of(font, FontMetadata.builder(Definition.builtin()).build()),
                winSkin.getTextboxRenderer(),
                faceRenderer,
                winSkin.getTextRenderer()
        );

        ModifierRegistry.init();
        TextParser parser = new TextParser();
        DataTracker ctx = DefaultDataTracker.of(ColorModifierNode.Parser.INDEXED_COLORS, winSkin.getIndexedColors());
        NodeList nodes = parser.parse(ctx, "Mercia:\n\\c[25]Hold on.");
        for (var node : nodes) {
            System.out.println(node);
        }

        Scene scene = new Scene(nodes, Map.of(faceRenderer.getDefaultFacePosition(), merciaFace),
                DefaultDataTracker.of(SceneMetadata.ARROW_FRAME, 1));

        var image = sceneRenderer.renderScene(scene);

        try (var out = Files.newOutputStream(outputPath)) {
            ImageIO.write(image, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write output image to \"%s\"".formatted(outputPath),
                    e);
        }
    }

    private static BufferedImage loadImage(String path) {
        try (var in = Resources.openStream(RPGMakerExtensionTest.class, path)) {
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image from \"%s\"".formatted(path), e);
        }
    }
}
