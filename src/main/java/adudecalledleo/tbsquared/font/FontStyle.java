package adudecalledleo.tbsquared.font;

public record FontStyle(boolean bold, boolean italic, boolean underline, boolean strikethrough, Superscript superscript,
                        int sizeAdjust) {
    public enum Superscript {
        MID, SUPER, SUB
    }

    public static final FontStyle DEFAULT = new FontStyle(false, false, false, false, Superscript.MID, 0);
}
