package adudecalledleo.tbsquared.parse.node;

import java.util.List;

public record NodeParsingContext(NodeRegistry registry) {
    public List<Node> parse(String contents) {
        // FIXME impl this!!!
        return List.of();
    }
}
