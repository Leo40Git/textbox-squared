package adudecalledleo.tbsquared.app;

import com.github.zafarkhaja.semver.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private Main() { }

    public static final Version VERSION = Version.forIntegers(1, 0, 0);
    public static final Version PLUGIN_API_VERSION = Version.forIntegers(1, 0, 0);

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
