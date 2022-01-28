package adudecalledleo.tbsquared.scene;

import java.util.Map;

import adudecalledleo.tbsquared.data.DataTracker;
import adudecalledleo.tbsquared.data.DefaultDataTracker;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.text.node.NodeList;

public record Scene(NodeList textNodes,
                    Map<FacePosition, Face> faces,
                    DataTracker metadata) {
    public Scene(NodeList textNodes, Map<FacePosition, Face> faces) {
        this(textNodes, faces, new DefaultDataTracker());
    }
}
