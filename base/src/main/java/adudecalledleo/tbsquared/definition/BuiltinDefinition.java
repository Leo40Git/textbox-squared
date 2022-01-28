package adudecalledleo.tbsquared.definition;

final class BuiltinDefinition extends Definition {
    static final Definition INSTANCE = new BuiltinDefinition();

    private BuiltinDefinition() {
        // FIXME use build info
        super("(built-in)", DEFAULT_DESCRIPTION, DEFAULT_CREDITS, null, null);
    }

    @Override
    public String getQualifiedName() {
        return name;
    }
}
