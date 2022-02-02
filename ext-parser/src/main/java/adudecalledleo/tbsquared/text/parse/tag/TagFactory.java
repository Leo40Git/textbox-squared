package adudecalledleo.tbsquared.text.parse.tag;

import java.util.Map;

@FunctionalInterface
public interface TagFactory {
    Tag createTag(Map<String, String> attributes);
}
