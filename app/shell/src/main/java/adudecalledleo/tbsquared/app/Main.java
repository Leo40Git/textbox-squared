package adudecalledleo.tbsquared.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private Main() { }

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
