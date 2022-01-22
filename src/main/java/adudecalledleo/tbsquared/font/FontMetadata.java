package adudecalledleo.tbsquared.font;

public record FontMetadata(boolean antialiasing) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean antialiasing;

        private Builder() {
            antialiasing = true;
        }

        public Builder antialiasing(boolean antialiasing) {
            this.antialiasing = antialiasing;
            return this;
        }

        public FontMetadata build() {
            return new FontMetadata(antialiasing);
        }
    }
}
