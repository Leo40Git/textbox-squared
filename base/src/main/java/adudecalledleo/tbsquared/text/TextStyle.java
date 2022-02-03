package adudecalledleo.tbsquared.text;

import java.awt.*;
import java.util.Optional;
import java.util.OptionalInt;

import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.util.TriState;
import org.jetbrains.annotations.Nullable;

public record TextStyle(Optional<Color> color, Optional<String> font,
                        TriState bold, TriState italic, TriState underline, TriState strikethrough,
                        Optional<FontStyle.Superscript> superscript, OptionalInt sizeAdjust) {
    public static final TextStyle EMPTY = new TextStyle(Optional.empty(), Optional.empty(),
            TriState.DEFAULT, TriState.DEFAULT, TriState.DEFAULT, TriState.DEFAULT,
            Optional.empty(), OptionalInt.empty());

    public FontStyle toFontStyle() {
        return new FontStyle(bold.toBoolean(false), italic.toBoolean(false),
                underline.toBoolean(false), strikethrough.toBoolean(false),
                superscript.orElse(FontStyle.Superscript.MID), sizeAdjust.orElse(0));
    }

    public TextStyle withColor(@Nullable Color color) {
        return new TextStyle(Optional.ofNullable(color), font, bold, italic, underline, strikethrough, superscript, sizeAdjust);
    }

    public TextStyle withFont(@Nullable String font) {
        return new TextStyle(color, Optional.ofNullable(font), bold, italic, underline, strikethrough, superscript, sizeAdjust);
    }

    public TextStyle withBold(boolean bold) {
        return new TextStyle(color, font, TriState.fromBool(bold), italic, underline, strikethrough, superscript, sizeAdjust);
    }

    public TextStyle withItalic(boolean italic) {
        return new TextStyle(color, font, bold, TriState.fromBool(italic), underline, strikethrough, superscript, sizeAdjust);
    }

    public TextStyle withUnderline(boolean underline) {
        return new TextStyle(color, font, bold, italic, TriState.fromBool(underline), strikethrough, superscript, sizeAdjust);
    }

    public TextStyle withStrikethrough(boolean strikethrough) {
        return new TextStyle(color, font, bold, italic, underline, TriState.fromBool(strikethrough), superscript, sizeAdjust);
    }

    public TextStyle withSuperscript(FontStyle.Superscript superscript) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, Optional.of(superscript), sizeAdjust);
    }

    public TextStyle withSizeAdjust(int sizeAdjust) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, superscript, OptionalInt.of(sizeAdjust));
    }
}
