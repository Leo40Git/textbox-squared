package adudecalledleo.tbsquared.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private Main() { }

    public static final Version VERSION = Version.forIntegers(1, 0, 0);
    public static final Version PLUGIN_API_VERSION = Version.forIntegers(1, 0, 0);

    public static final ObjectMapper JACKSON = new ObjectMapper()
            .registerModules(
                    new ParameterNamesModule(),
                    new Jdk8Module(),
                    new JavaTimeModule()
            );

    public static final Logger LOGGER;

    static {
        System.setProperty("log4j.skipJansi", "false"); // enable Log4J's Jansi support
        LOGGER = LoggerFactory.getLogger("textbox-squared");
    }

    public static void main(String[] args) {
        LOGGER.info("Hello world!");
        LOGGER.error("a");
    }
}
