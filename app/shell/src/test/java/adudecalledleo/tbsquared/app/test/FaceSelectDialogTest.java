package adudecalledleo.tbsquared.app.test;

import adudecalledleo.tbsquared.app.Bootstrap;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceCategoryRecipe;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FacePoolRecipe;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceRecipe;
import adudecalledleo.tbsquared.app.plugin.api.serialize.recipe.face.FaceRecipeException;
import adudecalledleo.tbsquared.app.plugin.api.util.ResourceImageLoader;
import adudecalledleo.tbsquared.app.ui.dialog.FaceSelectDialog;
import adudecalledleo.tbsquared.definition.Definition;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.FacePool;
import adudecalledleo.tbsquared.face.icon.FaceIconProvider;

public final class FaceSelectDialogTest {
    public static void main(String[] args) {
        Bootstrap.setSystemLookAndFeel();

        var facesBuilder = new FacePoolRecipe()
                .addCategory("mercia", new FaceCategoryRecipe()
                        .setIcon("neutral")
                        .addFace("neutral", new FaceRecipe("faces/mercia/neutral.png", FaceIconProvider.getDefault()))
                        .addFace("sad", new FaceRecipe("faces/mercia/sad.png", FaceIconProvider.getDefault()))
                        .addFace("angry", new FaceRecipe("faces/mercia/angry.png", FaceIconProvider.getDefault())))
                .addCategory("sixty", new FaceCategoryRecipe()
                        .setIcon("neutral")
                        .addFace("neutral", new FaceRecipe("faces/sixty/neutral.png", FaceIconProvider.getDefault())));
        FacePool faces;
        try {
            faces = facesBuilder.make(Definition.builtin(), new ResourceImageLoader(FaceSelectDialogTest.class, "/"));
        } catch (FaceRecipeException e) {
            throw new RuntimeException("failed to load faces", e);
        }

        Face currentFace = null;
        while (true) {
            var result = new FaceSelectDialog(null, faces, currentFace).showDialog();
            if (result == null) {
                break;
            }
            currentFace = result.right();
        }
        System.exit(0);
    }
}
