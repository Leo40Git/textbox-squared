package adudecalledleo.tbsquared.app;

import adudecalledleo.tbsquared.app.serialize.module.JSemVerModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public final class Main {
    private Main() { }

    public static final ObjectMapper JACKSON = new ObjectMapper()
            .registerModules(
                    new ParameterNamesModule(),
                    new Jdk8Module(),
                    new JavaTimeModule(),
                    new JSemVerModule());

    public static void main(String[] args) {
        Bootstrap.initPlugins();
    }
}
