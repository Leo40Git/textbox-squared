package adudecalledleo.tbsquared.text.parse.tag.basic;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;
import adudecalledleo.tbsquared.text.parse.tag.Tag;

final class STag extends Tag {
    public STag(Map<String, String> attributes) {
        super(BasicTags.STRIKETHROUGH, attributes);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withStrikethrough(true));
    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {
        textBuilder.style(style -> style.withStrikethrough(false));
    }
}
