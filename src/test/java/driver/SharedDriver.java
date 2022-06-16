package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import runner.GlobalConfig;

import java.util.concurrent.TimeUnit;

/**
 * Class wrapping the selenium drivers, right now it just implements google chrome
 */
public class SharedDriver {

	public SharedDriver() {
		if (DriverFactory.getDriver() == null) {
			ChromeOptions options = new ChromeOptions();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			options.setAcceptInsecureCerts(true);
			options.addArguments("test-type");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-web-security");
			options.addArguments("--allow-running-insecure-content");
			options.addArguments("--no-sandbox");
			if (GlobalConfig.HEADLESS) {
				options.addArguments("--headless");
				options.addArguments("window-size=1920,1080");
			}
			if (!GlobalConfig.PROXY.isEmpty()) {
				options.addArguments("--proxy-server=" + GlobalConfig.PROXY);
			}


			WebDriverManager.chromedriver().setup();
			WebDriver driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			//driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			driver.manage().deleteAllCookies();
			DriverFactory.addDriver(driver);
		}
	}

	public WebDriver getWebDriver() {
		return DriverFactory.getDriver();
	}
}
