package adudecalledleo.tbsquared.text.parse.tag.color;

import java.awt.*;

import adudecalledleo.tbsquared.data.DataTracker;

@Deprecated
@FunctionalInterface
public interface ColorSelector extends adudecalledleo.tbsquared.parse.node.color.ColorSelector {
    static ColorSelector parse(String value) {
        var delegate = adudecalledleo.tbsquared.parse.node.color.ColorSelector.parse(value);
        return new ColorSelector() {
            private final adudecalledleo.tbsquared.parse.node.color.ColorSelector _delegate = delegate;

            @Override
            public Color getColor(DataTracker ctx) {
                return _delegate.getColor(ctx);
            }
        };
    }
}
