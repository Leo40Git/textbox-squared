package adudecalledleo.tbsquared.text.parse.tag.basic;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

public final class SupTag extends Tag {
    public SupTag(Map<String, String> attributes) {
        super(BasicTags.SUPERSCRIPT, attributes);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.pushStyle(style -> style.withSuperscript(FontStyle.Superscript.SUPER));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.popStyle();
    }
}
