package adudecalledleo.tbsquared.font;

import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.definition.FromDefinition;

@SuppressWarnings("ClassCanBeRecord")
public final class FontMetadata implements FromDefinition {
    private final Definition sourceDefinition;
    private final boolean renderWithAntialiasing;

    public FontMetadata(Definition sourceDefinition, boolean renderWithAntialiasing) {
        this.sourceDefinition = sourceDefinition;
        this.renderWithAntialiasing = renderWithAntialiasing;
    }

    @Override
    public Definition getSourceDefinition() {
        return sourceDefinition;
    }

    public boolean shouldRenderWithAntialiasing() {
        return renderWithAntialiasing;
    }

    public static Builder builder(Definition sourceDefinition) {
        return new Builder(sourceDefinition);
    }

    public static final class Builder {
        private final Definition sourceDefinition;
        private boolean renderWithAntialiasing;

        private Builder(Definition sourceDefinition) {
            this.sourceDefinition = sourceDefinition;
            renderWithAntialiasing = true;
        }

        public Builder renderWithAntialiasing(boolean antialiasing) {
            this.renderWithAntialiasing = antialiasing;
            return this;
        }

        public FontMetadata build() {
            return new FontMetadata(sourceDefinition, renderWithAntialiasing);
        }
    }
}
