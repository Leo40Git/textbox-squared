package adudecalledleo.tbsquared.scene.composite;

import java.awt.*;
import java.awt.image.*;
import java.util.Collection;

import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.scene.FacePosition;
import adudecalledleo.tbsquared.scene.Scene;
import adudecalledleo.tbsquared.scene.SceneRenderer;
import adudecalledleo.tbsquared.util.render.HorizontalAlignment;
import adudecalledleo.tbsquared.util.render.VerticalAlignment;
import adudecalledleo.tbsquared.util.shape.Dim;
import adudecalledleo.tbsquared.util.shape.Rect;

public record CompositeSceneRenderer(Config config,
                                     SceneImageFactory imageFactory,
                                     FontProvider fonts,
                                     TextboxRenderer textboxRenderer,
                                     FaceRenderer faceRenderer,
                                     TextRenderer textRenderer) implements SceneRenderer {
    public static final class Builder {
        private Dim sceneSize;
        private Color sceneBackground;
        private Rect textboxRect;
        private Dim textboxPadding;
        private SceneImageFactory imageFactory;
        private FontProvider fonts;
        private TextboxRenderer textboxRenderer;
        private FaceRenderer faceRenderer;
        private TextRenderer textRenderer;

        private Builder() {
            imageFactory = SceneImageFactory.getDefault();
        }

        public Builder sceneSize(Dim sceneSize) {
            this.sceneSize = sceneSize;
            return this;
        }

        public Builder sceneSize(int width, int height) {
            return sceneSize(new Dim(width, height));
        }

        public Builder sceneBackground(Color sceneBackground) {
            this.sceneBackground = sceneBackground;
            return this;
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
            this.sceneBackground = config.sceneBackground();
            this.textboxRect = config.textboxRect();
            this.textboxPadding = config.textboxPadding();
            return this;
        }

        public Builder imageFactory(SceneImageFactory imageFactory) {
            if (imageFactory == null) {
                this.imageFactory = SceneImageFactory.getDefault();
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
            assertNotNull(sceneBackground, "sceneBackground");
            assertNotNull(textboxRect, "textboxRect");
            assertNotNull(textboxPadding, "textboxPadding");
            assertNotNull(imageFactory, "imageFactory");
            assertNotNull(fonts, "fonts");
            assertNotNull(textboxRenderer, "textboxRenderer");
            assertNotNull(faceRenderer, "faceRenderer");
            assertNotNull(textRenderer, "textRenderer");

            return new CompositeSceneRenderer(
                    new Config(sceneSize, sceneBackground, textboxRect, textboxPadding),
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

    public record Config(Dim sceneSize, Color sceneBackground, Rect textboxRect, Dim textboxPadding) { }

    @Override
    public Collection<? extends FacePosition> getFacePositions() {
        return faceRenderer.getFacePositions();
    }

    @Override
    public FacePosition getDefaultFacePosition() {
        return faceRenderer.getDefaultFacePosition();
    }

    @Override
    public BufferedImage renderScene(Scene scene) {
        BufferedImage image = imageFactory.createSceneImage(config.sceneSize().width(), config.sceneSize().height());
        var g = image.createGraphics();
        g.setBackground(config.sceneBackground());
        g.clearRect(0, 0, config.sceneSize().width(), config.sceneSize().height());
        textboxRenderer.renderBackground(g, scene.metadata(),
                config.textboxRect().x(), config.textboxRect().y(),
                config.textboxRect().width(), config.textboxRect().height());
        int x = config.textboxRect().x() + config.textboxPadding().width();
        int y = config.textboxRect().y() + config.textboxPadding().height();
        Dim facePadding = faceRenderer.renderFaces(g, scene.faces(), scene.metadata(), x, y);
        x += facePadding.width();
        y += facePadding.height();
        textRenderer.renderText(g, scene.text(), fonts, scene.metadata(), x, y);
        textboxRenderer.renderForeground(g, scene.metadata(),
                config.textboxRect().x(), config.textboxRect().y(),
                config.textboxRect().width(), config.textboxRect().height());
        g.dispose();
        return image;
    }
}
