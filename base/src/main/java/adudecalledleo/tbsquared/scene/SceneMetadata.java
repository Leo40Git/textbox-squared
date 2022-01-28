package adudecalledleo.tbsquared.scene;

import adudecalledleo.tbsquared.data.DataKey;

public final class SceneMetadata {
    private SceneMetadata() { }

    public static final DataKey<Integer> ARROW_FRAME = new DataKey<>(Integer.class, "arrow_frame");
    public static final DataKey<String> TEXTBOX_TITLE = new DataKey<>(String.class, "textbox_title");
}
