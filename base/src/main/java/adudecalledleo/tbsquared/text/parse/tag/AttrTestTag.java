package adudecalledleo.tbsquared.text.parse.tag;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;

public final class AttrTestTag extends Tag {
    public AttrTestTag(Map<String, String> attributes) {
        super("attrs", attributes);
        System.out.println(attributes);
    }

    @Override
    public void onOpen(DataTracker ctx, TextBuilder textBuilder) {

    }

    @Override
    public void onClose(DataTracker ctx, TextBuilder textBuilder) {

    }
}
