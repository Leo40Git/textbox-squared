package adudecalledleo.tbsquared.scene;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.text.Text;

public record Scene(Text text,
                    Map<FacePosition, Face> faces,
                    DataTracker metadata) {
    public Scene(Text text, Map<FacePosition, Face> faces) {
        this(text, faces, DataTracker.empty());
    }
}
