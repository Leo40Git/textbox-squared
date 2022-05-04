package adudecalledleo.tbsquared.app.plugin.test;

import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceCategoryRecipe;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FacePoolRecipe;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceRecipe;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;
import adudecalledleo.tbsquared.face.icon.ScalingFaceIconProvider;
import com.fasterxml.jackson.core.JsonProcessingException;

public final class FaceRecipeTest {
    public static void main(String[] args) {
        System.out.println(" === READING RECIPE === ");
        FacePoolRecipe recipe;
        try {
            recipe = TestPluginAPI.JACKSON.readValue("""
                    {
                      "Mercia": {
                        "icon": "Neutral",
                        "faces": {
                          "Neutral": "mercia/neutral.png",
                          "Blep": {
                            "path": "mercia/blep.png",
                            "comment": "Made by ADudeCalledLeo"
                          },
                          "TEST": {
                            "path": "test/test.png",
                            "icon": {
                               "type": "scale",
                               "factor": 0.5
                            }
                          },
                          "TEST2": {
                            "path": "test/test2.png",
                            "icon": {
                                "type": "crop",
                                "x": 0, "y": 0, "width": 16, "height": 16
                            }
                          },
                          "TEST3": {
                            "path": "test/test3.png"
                          }
                        }
                      }
                    }
                    """, FacePoolRecipe.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(recipe);

        System.out.println(" === WRITING RECIPE === ");
        FacePoolRecipe recipe2 = new FacePoolRecipe()
                .addCategory("Mercia", new FaceCategoryRecipe().setIcon("Neutral")
                        .addFace("Neutral", new FaceRecipe("mercia/neutral.png", FaceIconProvider.getDefault()))
                        .addFace("Blep", new FaceRecipe("mercia/blep.png", FaceIconProvider.getDefault(), "Made by ADudeCalledLeo"))
                        .addFace("TEST", new FaceRecipe("test/test.png", new ScalingFaceIconProvider(0.5), "abcd")));
        try {
            System.out.println(TestPluginAPI.JACKSON.writeValueAsString(recipe2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
