package driver;

import java.util.ArrayList;
import java.util.List;

import Shadow.ShadowQuery;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

/**
 * Class responsible for manage the web driver life cycle between threads.
 *
 */
public final class DriverFactory {

	private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

	// To quit the drivers and browsers at the end only.
	private static List<WebDriver> storedDrivers = new ArrayList<>();

	static {
		WebDriverManager.globalConfig().setTargetPath(ShadowQuery.class.getResource("/").getPath());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				for (WebDriver storedDriver : storedDrivers) {
					storedDriver.quit();
				}
			}
		});
	}

	private DriverFactory() {}

	public static WebDriver getDriver() {
		return drivers.get();
	}

	public static void addDriver(WebDriver driver) {
		storedDrivers.add(driver);
		drivers.set(driver);
	}

	public static void removeDriver() {
		storedDrivers.remove(drivers.get());
		drivers.remove();
	}
}
