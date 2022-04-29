package adudecalledleo.tbsquared.parse.node;

public abstract class Node {
    protected final String name;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
