package adudecalledleo.tbsquared.scene.composite;

import java.awt.image.*;
import java.util.Collection;
import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.text.Text;
import adudecalledleo.tbsquared.util.render.HorizontalAlignment;
import adudecalledleo.tbsquared.util.render.VerticalAlignment;
import adudecalledleo.tbsquared.util.shape.Dim;
import adudecalledleo.tbsquared.util.shape.Rect;

public record CompositeSceneRenderer(Config config,
                                     ImageFactory imageFactory,
                                     FontProvider fonts,
                                     TextboxRenderer textboxRenderer,
                                     FaceRenderer faceRenderer,
                                     TextRenderer textRenderer) implements SceneRenderer {
    public static final class Builder {
        private Dim sceneSize;
        private Rect textboxRect;
        private Dim textboxPadding;
        private ImageFactory imageFactory;
        private FontProvider fonts;
        private TextboxRenderer textboxRenderer;
        private FaceRenderer faceRenderer;
        private TextRenderer textRenderer;

        private Builder() {
            imageFactory = ImageFactory.BLANK;
        }

        public Builder sceneSize(Dim sceneSize) {
            this.sceneSize = sceneSize;
            return this;
        }

        public Builder sceneSize(int width, int height) {
            return sceneSize(new Dim(width, height));
        }

        public Builder textboxRect(Rect textboxRect) {
            this.textboxRect = textboxRect;
            return this;
        }

        public Builder textboxRect(int x, int y, int width, int height) {
            return textboxRect(new Rect(x, y, width, height));
        }

        public Builder textboxRect(int width, int height, HorizontalAlignment alignX, VerticalAlignment alignY) {
            assertNotNull(sceneSize, "sceneSize");
            return textboxRect(new Rect(
                    alignX.align(sceneSize.width(), width), alignY.align(sceneSize.height(), height),
                    width, height));
        }

        public Builder textboxPadding(Dim textboxPadding) {
            this.textboxPadding = textboxPadding;
            return this;
        }

        public Builder textboxPadding(int width, int height) {
            return textboxPadding(new Dim(width, height));
        }

        public Builder config(Config config) {
            this.sceneSize = config.sceneSize();
            this.textboxRect = config.textboxRect();
            this.textboxPadding = config.textboxPadding();
            return this;
        }

        public Builder imageFactory(ImageFactory imageFactory) {
            if (imageFactory == null) {
                this.imageFactory = ImageFactory.BLANK;
            } else {
                this.imageFactory = imageFactory;
            }
            return this;
        }

        public Builder fonts(FontProvider fonts) {
            this.fonts = fonts;
            return this;
        }

        public Builder textboxRenderer(TextboxRenderer textboxRenderer) {
            this.textboxRenderer = textboxRenderer;
            return this;
        }

        public Builder faceRenderer(FaceRenderer faceRenderer) {
            this.faceRenderer = faceRenderer;
            return this;
        }

        public Builder textRenderer(TextRenderer textRenderer) {
            this.textRenderer = textRenderer;
            return this;
        }

        private void assertNotNull(Object value, String name) {
            if (value == null) {
                throw new IllegalArgumentException(name + " is null");
            }
        }

        public CompositeSceneRenderer build() {
            assertNotNull(sceneSize, "sceneSize");
            assertNotNull(textboxRect, "textboxRect");
            assertNotNull(textboxPadding, "textboxPadding");
            assertNotNull(imageFactory, "imageFactory");
            assertNotNull(fonts, "fonts");
            assertNotNull(textboxRenderer, "textboxRenderer");
            assertNotNull(faceRenderer, "faceRenderer");
            assertNotNull(textRenderer, "textRenderer");

            return new CompositeSceneRenderer(
                    new Config(sceneSize, textboxRect, textboxPadding),
                    imageFactory,
                    fonts,
                    textboxRenderer,
                    faceRenderer,
                    textRenderer);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public record Config(Dim sceneSize, Rect textboxRect, Dim textboxPadding) { }

    @Override
    public Collection<? extends FacePosition> getFacePositions() {
        return faceRenderer.getFacePositions();
    }

    @Override
    public FacePosition getDefaultFacePosition() {
        return faceRenderer.getDefaultFacePosition();
    }

    @Override
    public BufferedImage renderScene(Text text, Map<FacePosition, Face> faces, DataTracker metadata) {
        BufferedImage image = imageFactory.createImage(config.sceneSize());
        var g = image.createGraphics();
        textboxRenderer.renderBackground(g, metadata,
                config.textboxRect().x(), config.textboxRect().y(),
                config.textboxRect().width(), config.textboxRect().height());
        int x = config.textboxRect().x() + config.textboxPadding().width();
        int y = config.textboxRect().y() + config.textboxPadding().height();
        Dim facePadding = faceRenderer.renderFaces(g, faces, metadata, x, y);
        x += facePadding.width();
        y += facePadding.height();
        textRenderer.renderText(g, text, fonts, metadata, x, y);
        textboxRenderer.renderForeground(g, metadata,
                config.textboxRect().x(), config.textboxRect().y(),
                config.textboxRect().width(), config.textboxRect().height());
        g.dispose();
        return image;
    }
}
