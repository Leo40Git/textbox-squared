package adudecalledleo.tbsquared.definition;

import java.nio.file.Path;

final class BuiltinDefinition extends Definition {
    static final Definition INSTANCE = new BuiltinDefinition();

    private BuiltinDefinition() {
        // TODO description and credits (from build info?)
        super("(built-in)", DEFAULT_DESCRIPTION, DEFAULT_CREDITS, null, null);
    }

    @Override
    public Path getFilePath() {
        throw new UnsupportedOperationException("Can't get file path of built-in definition");
    }

    @Override
    public Path getBasePath() {
        throw new UnsupportedOperationException("Can't get base path of built-in definition");
    }

    @Override
    public String getQualifiedName() {
        return name;
    }
}
