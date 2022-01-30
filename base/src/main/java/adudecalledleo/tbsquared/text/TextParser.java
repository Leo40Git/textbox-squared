package adudecalledleo.tbsquared.text;

import adudecalledleo.tbsquared.data.DataTracker;

public final class TextParser {
    private final StringBuilder sb;

    public TextParser() {
        sb = new StringBuilder();
    }

    public Text parse(DataTracker ctx, String text) {
        text = TextSanitizer.apply(text);
        // TODO
        return new Text(TextStyle.EMPTY, "(TODO!!!)");
    }
}
