package adudecalledleo.tbsquared.face;

import java.util.*;

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
                iconFaceName = face.getName();
            }
            return this;
        }

        public Builder addFaces(Collection<Face> faces) {
            this.faces.addAll(faces);
            if (!faces.isEmpty() && iconFaceName == null) {
                iconFaceName = faces.iterator().next().getName();
            }
            return this;
        }

        public Builder addFaces(Face... faces) {
            Collections.addAll(this.faces, faces);
            if (faces.length > 0 && iconFaceName == null) {
                iconFaceName = faces[0].getName();
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
    private final Map<String, Face> faces;
    private final Face iconFace;

    public FaceCategory(String name, List<Face> faces, String iconFaceName) {
        this.name = name;
        Map<String, Face> facesMap = new LinkedHashMap<>(faces.size());
        for (var face : faces) {
            facesMap.put(face.getName(), face);
        }
        this.faces = Map.copyOf(facesMap);
        try {
            this.iconFace = getFace(iconFaceName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Icon face \"%s\" doesn't exist in face list".formatted(iconFaceName));
        }
    }

    public String getName() {
        return name;
    }

    public Collection<Face> getFaces() {
        return faces.values();
    }

    public String getIconFaceName() {
        return iconFace.getName();
    }

    public Face getFace(String name) {
        var face = faces.get(name);
        if (face == null) {
            throw new IllegalArgumentException("Unknown face \"%s\"".formatted(name));
        }
        return face;
    }

    public Face getIconFace() {
        return iconFace;
    }
}
