package adudecalledleo.tbsquared.parse.node;

public abstract class AbstractNode implements Node {
    protected final String name;

    public AbstractNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
