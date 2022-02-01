package adudecalledleo.tbsquared.text.parse.tag.basic;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.font.FontStyle;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

public final class SubTag extends Tag {
    public SubTag(Map<String, String> attributes) {
        super(BasicTags.SUBSCRIPT, attributes);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.pushStyle(style -> style.withSuperscript(FontStyle.Superscript.SUB));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.popStyle();
    }
}
