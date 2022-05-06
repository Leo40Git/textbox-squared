package adudecalledleo.tbsquared.app;

import adudecalledleo.tbsquared.app.plugin.api.serialize.module.FaceRecipeModule;
import adudecalledleo.tbsquared.app.plugin.api.serialize.module.JSemVerModule;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public final class Main {
    private Main() { }

    public static final ObjectMapper JACKSON = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS, JsonReadFeature.ALLOW_SINGLE_QUOTES,
                    JsonReadFeature.ALLOW_TRAILING_COMMA, JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
            .build()
            .registerModules(
                    new ParameterNamesModule(),
                    new Jdk8Module(),
                    new JavaTimeModule(),
                    new FaceRecipeModule(),
                    new JSemVerModule());

    public static void main(String[] args) {
        Bootstrap.initPlugins();
    }
}
