package adudecalledleo.tbsquared.parse.node.style;

import java.util.List;
import java.util.Map;

import adudecalledleo.tbsquared.icon.IconPool;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.*;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.text.TextBuilder;
import org.jetbrains.annotations.Nullable;

public final class IconNode extends ContainerNode {
    public static final String NAME = "icon";
    public static final NodeHandler<IconNode> HANDLER = new Handler();

    public static void register(NodeRegistry registry) {
        registry.register(NAME, HANDLER);
    }

    private final String iconName;

    public IconNode(String iconName, Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, List<Node> children) {
        super(NAME, openingSpan, closingSpan, attributes, children);
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }

    private static final class Handler implements NodeHandler<IconNode> {
        @Override
        public @Nullable IconNode parse(NodeParsingContext ctx, int offset, List<DOMParser.Error> errors,
                                        Span openingSpan, Span closingSpan, Map<String, Attribute> attributes, String contents) {
            var iconAttr = attributes.get("value");
            if (iconAttr == null) {
                errors.add(new DOMParser.Error(openingSpan.start(), openingSpan.length(), "Missing required attribute \"value\""));
                return null;
            }

            if (isAttributeBlank(iconAttr, errors)) {
                return null;
            }

            var iconStr = iconAttr.value().trim();
            var icons = ctx.metadata().get(IconPool.ICONS).orElse(null);
            if (icons != null) {
                if (!icons.hasIcon(iconStr)) {
                    errors.add(new DOMParser.Error(iconAttr.valueSpan().start(), iconAttr.valueSpan().length(),
                            "unknown icon \"" + iconStr + "\""));
                    return null;
                }
            }

            return new IconNode(iconStr, openingSpan, closingSpan, attributes, ctx.parse(contents, offset, errors));
        }

        @Override
        public void convert(NodeConversionContext ctx, IconNode node, TextBuilder tb) {
            tb.pushStyle(style -> style.withExtras(extras -> extras.set(IconPool.PREFIX_ICON, node.getIconName())));
            ctx.convert(node.getChildren(), tb);
            tb.popStyle();
        }
    }
}
