package adudecalledleo.tbsquared.scene;

import java.util.Map;

import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.metadata.DefaultMetadataTracker;
import adudecalledleo.tbsquared.metadata.MetadataTracker;
import adudecalledleo.tbsquared.text.node.NodeList;

public record Scene(NodeList textNodes,
                    Map<FacePosition, Face> faces,
                    MetadataTracker metadata) {
    public Scene(NodeList textNodes, Map<FacePosition, Face> faces) {
        this(textNodes, faces, new DefaultMetadataTracker());
    }
}
