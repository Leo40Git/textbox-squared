package adudecalledleo.tbsquared.app.plugin.test;

import adudecalledleo.tbsquared.app.plugin.serialize.recipe.face.FaceCategoryRecipe;
import adudecalledleo.tbsquared.app.plugin.serialize.recipe.face.FacePoolRecipe;
import adudecalledleo.tbsquared.app.plugin.serialize.recipe.face.FaceRecipe;
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
                        .addFace("Neutral", new FaceRecipe("mercia/neutral.png"))
                        .addFace("Blep", new FaceRecipe("mercia/blep.png", "Made by ADudeCalledLeo")));
        try {
            System.out.println(TestPluginAPI.JACKSON.writeValueAsString(recipe2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
