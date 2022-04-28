package adudecalledleo.tbsquared.app.plugin.test;

import adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face.FaceCategoryDescriptor;
import adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face.FaceDescriptor;
import adudecalledleo.tbsquared.app.plugin.serialize.descriptor.face.FacePoolDescriptor;
import com.fasterxml.jackson.core.JsonProcessingException;

public final class FaceDescriptorTest {
    public static void main(String[] args) {
        System.out.println(" === READING DESCRIPTOR === ");
        FacePoolDescriptor descriptor;
        try {
            descriptor = TestPluginAPI.JACKSON.readValue("""
                    {
                      "Mercia": {
                        "icon": "Neutral",
                        "faces": {
                          "Neutral": "mercia/neutral.png",
                          "Blep": {
                            "path": "mercia/blep.png",
                            "comment": "Made by ADudeCalledLeo"
                          }
                        }
                      }
                    }
                    """, FacePoolDescriptor.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(descriptor);

        System.out.println(" === WRITING DESCRIPTOR === ");
        FaceCategoryDescriptor categoryDescriptor = new FaceCategoryDescriptor();
        categoryDescriptor.icon = "Neutral";
        categoryDescriptor.faces.put("Neutral", new FaceDescriptor("mercia/neutral.png", ""));
        categoryDescriptor.faces.put("Blep", new FaceDescriptor("mercia/blep.png", "Made by ADudeCalledLeo"));
        FacePoolDescriptor descriptor2 = new FacePoolDescriptor();
        descriptor2.addCategory("Mercia", categoryDescriptor);
        try {
            System.out.println(TestPluginAPI.JACKSON.writeValueAsString(descriptor2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
