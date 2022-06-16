package stepdef;

import driver.SharedDriver;
import io.cucumber.core.api.Scenario;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import runner.GlobalConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Hooks {
    public static Logger log = LoggerFactory.getLogger(Hooks.class);
    private final WebDriver driver;
    private final SharedData sharedData;

    public Hooks(SharedDriver sharedDriver, SharedData sharedData) {
        this.driver = sharedDriver.getWebDriver();
        this.sharedData = sharedData;
    }

    @AfterStep
    public void afterScenario(Scenario scenario) throws IOException {
        log.info("Taking screenshot");
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
        for (String x : sharedData.getAttachments()) {
            File file = new File(x);
            scenario.embed(IOUtils.toByteArray(Files.newInputStream(file.toPath())), Files.probeContentType(file.toPath()));
        }
        log.info("Closing session");
//        driver.get(GlobalConfig.URL + "/logout");
    }


}

