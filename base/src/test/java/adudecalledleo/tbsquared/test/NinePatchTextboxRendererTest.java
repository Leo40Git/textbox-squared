package adudecalledleo.tbsquared.test;

import java.awt.image.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.composite.impl.NinePatchTextboxRenderer;
import adudecalledleo.tbsquared.util.Resources;
import adudecalledleo.tbsquared.util.render.Colors;

public final class NinePatchTextboxRendererTest {
    private NinePatchTextboxRendererTest() { }

    public static void main(String[] args) {
        String sourcePath = "/ninepatch.png";
        Path outputPath = Paths.get(".", "output.png").toAbsolutePath();

        BufferedImage sourceImage;
        try (var in = Resources.openStream(NinePatchTextboxRendererTest.class, sourcePath)) {
            sourceImage = ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read source image from \"%s\"".formatted(sourcePath),
                    e);
        }

        var renderer = new NinePatchTextboxRenderer(sourceImage);

        BufferedImage scratchImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        var g = scratchImage.createGraphics();
        g.setBackground(Colors.TRANSPARENT);
        g.clearRect(0, 0, scratchImage.getWidth(), scratchImage.getHeight());
        renderer.renderBackground(g, DataTracker.empty(), 0, 0, 128, 64);
        renderer.renderBackground(g, DataTracker.empty(), 200, 200, 300, 200);
        renderer.renderBackground(g, DataTracker.empty(), 100, 500, 400, 80);
        g.dispose();
        try (var out = Files.newOutputStream(outputPath)) {
            ImageIO.write(scratchImage, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write output image to \"%s\"".formatted(outputPath),
                    e);
        }
    }
}
