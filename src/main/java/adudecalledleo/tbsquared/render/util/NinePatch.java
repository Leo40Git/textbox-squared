package adudecalledleo.tbsquared.render.util;

import java.awt.*;
import java.awt.image.*;

public final class NinePatch {
    private static final int PIECE_TL = 0;
    private static final int PIECE_TM = 1;
    private static final int PIECE_TR = 2;
    private static final int PIECE_CL = 3;
    private static final int PIECE_CM = 4;
    private static final int PIECE_CR = 5;
    private static final int PIECE_BL = 6;
    private static final int PIECE_BM = 7;
    private static final int PIECE_BR = 8;
    private static final int PIECE_MAX = 9;

    private final BufferedImage sourceImage;

    private final int pieceWidth;
    private final int pieceHeight;
    private final int[][] pieceOrigins;

    public NinePatch(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;

        pieceOrigins = new int[PIECE_MAX][2];

        pieceWidth = sourceImage.getWidth() / 3;
        pieceHeight = sourceImage.getHeight() / 3;
        int pieceX = 0, pieceY = 0;
        for (int i = 0; i < PIECE_MAX; i++) {
            pieceOrigins[i][0] = pieceX;
            pieceOrigins[i][1] = pieceY;

            pieceX += pieceWidth;
            if (pieceX >= sourceImage.getWidth()) {
                pieceX = 0;
                pieceY += pieceHeight;
            }
        }
    }

    public void render(Graphics g, int x, int y, int width, int height) {
        // TOP
        renderPiece(g, PIECE_TL, x, y, pieceWidth, pieceHeight);
        renderPiece(g, PIECE_TM, x + pieceWidth, y, width - pieceWidth * 2, pieceHeight);
        renderPiece(g, PIECE_TR, x + width - pieceWidth, y, pieceWidth, pieceHeight);
        // MIDDLE
        renderPiece(g, PIECE_CL, x, y + pieceHeight, pieceWidth, height - pieceHeight * 2);
        renderPiece(g, PIECE_CM, x + pieceWidth, y + pieceHeight, width - pieceWidth * 2, height - pieceHeight * 2);
        renderPiece(g, PIECE_CR, x + width - pieceWidth, y + pieceHeight, pieceWidth, height - pieceHeight * 2);
        // BOTTOM
        renderPiece(g, PIECE_BL, x, y + height - pieceHeight, pieceWidth, pieceHeight);
        renderPiece(g, PIECE_BM, x + pieceWidth, y + height - pieceHeight, width - pieceWidth * 2, pieceHeight);
        renderPiece(g, PIECE_BR, x + width - pieceWidth, y + height - pieceHeight, pieceWidth, pieceHeight);
    }

    private void renderPiece(Graphics g, int piece, int x, int y, int width, int height) {
        final int sx = pieceOrigins[piece][0];
        final int sy = pieceOrigins[piece][1];
        g.drawImage(sourceImage,
                x, y, x + width, y + height,
                sx, sy, sx + pieceWidth, sy + pieceHeight,
                null);
    }
}
