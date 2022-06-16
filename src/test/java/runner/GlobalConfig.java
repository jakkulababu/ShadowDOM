package runner;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Class hosting constants for different parts of the testing
 */
public class GlobalConfig {
    /**
     * Root url for EC
     */
    public static final String URL;
    /**
     * Polling time for the application
     */
    public static final Duration POLLING;
    /**
     * Maximum waiting time for a response of the application
     */
    public static final Duration TIMEOUT;
    /**
     * Test execution in headless mode
     */
    public static final boolean HEADLESS;
    /**
     * The proxy for web driver
     */
    public static final String PROXY;

    private static final Logger log = LoggerFactory.getLogger(GlobalConfig.class);

    static {
        Properties prop = new Properties();
        try {
            prop.load(GlobalConfig.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            log.error("Can not init config", ex);
        }
        URL = prop.getProperty("URL");
        POLLING = Duration.ofMillis(Long.parseLong(prop.getProperty("polling")));
        TIMEOUT = Duration.ofMillis(Long.parseLong(prop.getProperty("timeout")));
        HEADLESS = Boolean.parseBoolean(prop.getProperty("headless"));
        PROXY = prop.getProperty("proxy");


    }

}
