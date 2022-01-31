package adudecalledleo.tbsquared.text.parse.tag;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.text.TextBuilder;

public abstract class Tag {
    protected final String name;
    protected final Map<String, String> attributes;

    public Tag(String name, Map<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public final String getName() {
        return name;
    }

    public abstract void onOpen(DataTracker ctx, TextBuilder textBuilder);
    public abstract void onClose(DataTracker ctx, TextBuilder textBuilder);
}
