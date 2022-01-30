package adudecalledleo.tbsquared.text.parse.tag.basic;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

final class UTag extends Tag {

    public UTag(Map<String, String> attributes) {
        super(BasicTags.UNDERLINE, attributes);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withUnderline(true));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withUnderline(false));
    }
}
