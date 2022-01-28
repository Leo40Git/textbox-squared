package adudecalledleo.tbsquared.definition;

import java.nio.file.Path;

public abstract class Definition {
    public static final String[] DEFAULT_DESCRIPTION = { "(no description)" };
    public static final String[] DEFAULT_CREDITS = new String[0];

    protected final String name;
    protected final String[] description, credits;
    protected final Path filePath, basePath;

    public Definition(String name, String[] description, String[] credits, Path filePath, Path basePath) {
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.filePath = filePath;
        this.basePath = basePath;
    }

    public static Definition builtin() {
        return BuiltinDefinition.INSTANCE;
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getCredits() {
        return credits;
    }

    public Path getFilePath() {
        return filePath;
    }

    public Path getBasePath() {
        return basePath;
    }

    public abstract String getQualifiedName();
}
