package adudecalledleo.tbsquared.rpgmaker;

import java.awt.*;
import java.awt.image.*;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.scene.SceneMetadata;
import adudecalledleo.tbsquared.scene.composite.TextboxRenderer;
import adudecalledleo.tbsquared.util.shape.Rect;

final class RPGTextboxRenderer implements TextboxRenderer {
    private final BufferedImage windowImage;
    private final RPGBackgroundRenderer backgroundRenderer;
    private final int flags;

    private final int borderPieceSize;
    private final int borderMPieceWidth, borderCPieceHeight;
    private static final int BORDER_PIECE_TL = 0;
    private static final int BORDER_PIECE_TM = 1;
    private static final int BORDER_PIECE_TR = 2;
    private static final int BORDER_PIECE_CL = 3;
    private static final int BORDER_PIECE_CR = 4;
    private static final int BORDER_PIECE_BL = 5;
    private static final int BORDER_PIECE_BM = 6;
    private static final int BORDER_PIECE_BR = 7;
    private static final int BORDER_PIECE_MAX = 8;
    private final Rect[] borderPieces;

    private final int arrowFrameSize;
    private final Rect[] arrowFrames;

    public RPGTextboxRenderer(RPGWindowSkin.Version version, BufferedImage windowImage,
                              RPGBackgroundRenderer backgroundRenderer, int flags) {
        this.windowImage = windowImage;
        this.backgroundRenderer = backgroundRenderer;
        this.flags = flags;

        /// BORDER
        borderPieceSize = version.scale(16);
        borderMPieceWidth = borderCPieceHeight = version.scale(32);
        final int borderStartX = version.scale(64);
        final int borderMPad = version.scale(32);
        borderPieces = new Rect[BORDER_PIECE_MAX];
        // TOP
        borderPieces[BORDER_PIECE_TL] = new Rect(borderStartX, 0, borderPieceSize, borderPieceSize);
        borderPieces[BORDER_PIECE_TM] = new Rect(borderStartX + borderPieceSize, 0, borderMPieceWidth, borderPieceSize);
        borderPieces[BORDER_PIECE_TR] = new Rect(borderStartX + borderPieceSize + borderMPieceWidth, 0, borderPieceSize, borderPieceSize);
        // CENTER
        borderPieces[BORDER_PIECE_CL] = new Rect(borderStartX, borderPieceSize, borderPieceSize, borderCPieceHeight);
        borderPieces[BORDER_PIECE_CR] = new Rect(borderStartX + borderPieceSize + borderMPad, borderPieceSize, borderPieceSize, borderCPieceHeight);
        // BOTTOM
        borderPieces[BORDER_PIECE_BL] = new Rect(borderStartX, borderPieceSize + borderCPieceHeight, borderPieceSize, borderPieceSize);
        borderPieces[BORDER_PIECE_BM] = new Rect(borderStartX + borderPieceSize, borderPieceSize + borderCPieceHeight, borderMPieceWidth, borderPieceSize);
        borderPieces[BORDER_PIECE_BR] = new Rect(borderStartX + borderPieceSize + borderMPieceWidth, borderPieceSize + borderCPieceHeight, borderPieceSize, borderPieceSize);

        /// ARROW
        arrowFrameSize = version.scale(16);
        arrowFrames = new Rect[4];
        final int arrowStartX = version.scale(96), arrowStartY = version.scale(64);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                arrowFrames[y * 2 + x] = new Rect(
                        arrowStartX + (x * arrowFrameSize), arrowStartY + (y * arrowFrameSize),
                        arrowFrameSize, arrowFrameSize);
            }
        }
    }

    @Override
    public void renderBackground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        backgroundRenderer.renderBackground(g, x, y, width, height);
        if ((flags & RPGWindowSkin.TEXTBOX_BORDER_IN_BACKGROUND) != 0) {
            renderTextboxBorder(g, x, y, width, height);
        }
        if ((flags & RPGWindowSkin.TEXTBOX_ARROW_IN_BACKGROUND) != 0) {
            renderTextboxArrow(g, sceneMeta, x, y, width, height);
        }
    }

    @Override
    public void renderForeground(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        if ((flags & RPGWindowSkin.TEXTBOX_BORDER_IN_BACKGROUND) == 0) {
            renderTextboxBorder(g, x, y, width, height);
        }
        if ((flags & RPGWindowSkin.TEXTBOX_ARROW_IN_BACKGROUND) == 0) {
            renderTextboxArrow(g, sceneMeta, x, y, width, height);
        }
    }

    private void renderTextboxBorder(Graphics2D g, int x, int y, int width, int height) {
        // TOP
        drawPiece(g, borderPieces[BORDER_PIECE_TL], x, y);
        drawPiece(g, borderPieces[BORDER_PIECE_TM], x + borderPieceSize, y, width - borderMPieceWidth, borderPieceSize);
        drawPiece(g, borderPieces[BORDER_PIECE_TR], x + width - borderPieceSize, y);
        // CENTER
        drawPiece(g, borderPieces[BORDER_PIECE_CL], x, y + borderPieceSize, borderPieceSize, height - borderCPieceHeight);
        drawPiece(g, borderPieces[BORDER_PIECE_CR], x + width - borderPieceSize, y + borderPieceSize, borderPieceSize, height - borderCPieceHeight);
        // BOTTOM
        drawPiece(g, borderPieces[BORDER_PIECE_BL], x, y + height - borderPieceSize);
        drawPiece(g, borderPieces[BORDER_PIECE_BM], x + borderPieceSize, y + height - borderPieceSize, width - borderMPieceWidth, borderPieceSize);
        drawPiece(g, borderPieces[BORDER_PIECE_BR], x + width - borderPieceSize, y + height - borderPieceSize);
    }

    private void renderTextboxArrow(Graphics2D g, DataTracker sceneMeta, int x, int y, int width, int height) {
        sceneMeta.get(SceneMetadata.ARROW_FRAME).ifPresent(arrowFrame ->
                drawPiece(g, arrowFrames[arrowFrame],
                        x + (width / 2) - (arrowFrameSize / 2),
                        y + height - arrowFrameSize));
    }

    private void drawPiece(Graphics2D g, Rect piece, int x, int y, int width, int height) {
        g.drawImage(windowImage,
                x, y, x + width, y + height,
                piece.x1(), piece.y1(), piece.x2(), piece.y2(),
                null);
    }

    private void drawPiece(Graphics2D g, Rect piece, int x, int y) {
        drawPiece(g, piece, x, y, piece.width(), piece.height());
    }
}
