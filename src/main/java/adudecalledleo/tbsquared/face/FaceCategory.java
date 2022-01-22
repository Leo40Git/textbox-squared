package adudecalledleo.tbsquared.face;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FaceCategory {
    public static final class Builder {
        private final String name;
        private final List<Face> faces;
        private String iconFaceName;

        private Builder(String name) {
            this.name = name;
            faces = new ArrayList<>();
        }

        public Builder addFace(Face face) {
            faces.add(face);
            if (iconFaceName == null) {
                iconFaceName = face.name();
            }
            return this;
        }

        public Builder addFaces(Collection<Face> faces) {
            this.faces.addAll(faces);
            if (!faces.isEmpty() && iconFaceName == null) {
                iconFaceName = faces.iterator().next().name();
            }
            return this;
        }

        public Builder addFaces(Face... faces) {
            Collections.addAll(this.faces, faces);
            if (faces.length > 0 && iconFaceName == null) {
                iconFaceName = faces[0].name();
            }
            return this;
        }

        public Builder iconFace(String name) {
            iconFaceName = name;
            return this;
        }

        public FaceCategory build() {
            return new FaceCategory(name, faces, iconFaceName);
        }
    }

    private final String name;
    private final List<Face> faces;
    private final Face iconFace;

    public FaceCategory(String name, List<Face> faces, String iconFaceName) {
        this.name = name;
        this.faces = List.copyOf(faces);
        try {
            this.iconFace = getFace(iconFaceName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Icon face doesn't exist in face list!");
        }
    }

    public String getName() {
        return name;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public String getIconFaceName() {
        return iconFace.name();
    }

    public Face getFace(String name) {
        for (var portrait : faces) {
            if (name.equals(portrait.name())) {
                return portrait;
            }
        }
        throw new IllegalArgumentException("Unknown face \"%s\"".formatted(name));
    }

    public Face getIconFace() {
        return iconFace;
    }
}
