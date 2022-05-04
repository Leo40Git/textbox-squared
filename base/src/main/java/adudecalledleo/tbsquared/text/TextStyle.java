package adudecalledleo.tbsquared.text;

import java.awt.*;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.UnaryOperator;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.data.MutableDataTracker;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.util.TriState;
import org.jetbrains.annotations.NotNull;

public record TextStyle(Optional<Color> color, Optional<String> font,
                        TriState bold, TriState italic, TriState underline, TriState strikethrough,
                        Optional<FontStyle.Superscript> superscript, OptionalInt sizeAdjust,
                        DataTracker extras) {
    public static final TextStyle EMPTY = new TextStyle(Optional.empty(), Optional.empty(),
            TriState.DEFAULT, TriState.DEFAULT, TriState.DEFAULT, TriState.DEFAULT,
            Optional.empty(), OptionalInt.empty(),
            DataTracker.empty());

    public FontStyle toFontStyle() {
        return new FontStyle(bold.toBoolean(false), italic.toBoolean(false),
                underline.toBoolean(false), strikethrough.toBoolean(false),
                superscript.orElse(FontStyle.Superscript.MID), sizeAdjust.orElse(0));
    }

    public TextStyle withColor(@NotNull Color color) {
        return new TextStyle(Optional.of(color), font, bold, italic, underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withDefaultColor() {
        return new TextStyle(Optional.empty(), font, bold, italic, underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withFont(@NotNull String font) {
        return new TextStyle(color, Optional.of(font), bold, italic, underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withDefaultFont() {
        return new TextStyle(color, Optional.empty(), bold, italic, underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withBold(boolean bold) {
        return new TextStyle(color, font, TriState.fromBool(bold), italic, underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withItalic(boolean italic) {
        return new TextStyle(color, font, bold, TriState.fromBool(italic), underline, strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withUnderline(boolean underline) {
        return new TextStyle(color, font, bold, italic, TriState.fromBool(underline), strikethrough, superscript, sizeAdjust, extras);
    }

    public TextStyle withStrikethrough(boolean strikethrough) {
        return new TextStyle(color, font, bold, italic, underline, TriState.fromBool(strikethrough), superscript, sizeAdjust, extras);
    }

    public TextStyle withSuperscript(@NotNull FontStyle.Superscript superscript) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, Optional.of(superscript), sizeAdjust, extras);
    }

    public TextStyle withSizeAdjust(int sizeAdjust) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, superscript, OptionalInt.of(sizeAdjust), extras);
    }

    public TextStyle withExtras(DataTracker extras) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, superscript, sizeAdjust, DataTracker.copyOf(extras));
    }

    public TextStyle withExtras(UnaryOperator<MutableDataTracker> extrasModifier) {
        return new TextStyle(color, font, bold, italic, underline, strikethrough, superscript, sizeAdjust,
                DataTracker.copyOf(extrasModifier.apply(MutableDataTracker.copyOf(extras))));
    }
}
