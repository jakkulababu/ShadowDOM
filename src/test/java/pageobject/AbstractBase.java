package pageobject;

import Shadow.LocatorFactory;
import Shadow.ShadowQuery;
import driver.SharedDriver;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import runner.GlobalConfig;

import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * I am changing the behavior, to reduce the boilerplate code, the next part is form the parent docs:
 * <p>
 * Represents any abstraction of something that can be loaded. This may be an entire web page, or
 * simply a component within that page (such as a login box or menu) or even a service. The expected
 * usage is:
 *
 * <pre class="code">
 * new HypotheticalComponent().get();
 * </pre>
 *
 * <p>
 * After the {@link LoadableComponent#get()} method is called, the component will be loaded and
 * ready for use. This is verified using Assert.assertTrue so expect to catch an Error rather than
 * an Exception when errors occur. *
 *
 * @param <T> The type to be returned (normally the subclass' type)
 */
public abstract class AbstractBase<T extends AbstractBase> extends LoadableComponent {
    private static final Log log = LogFactory.getLog(AbstractBase.class);
    private final WebDriver driver;

    protected abstract By getInitElement();

    /**
     * Usually there is not need to instantiate this manually, picocontainer does it for you
     *
     * @param sharedDriver the wrapper of the selenium driver
     */
    public AbstractBase(SharedDriver sharedDriver) {
        this.driver = sharedDriver.getWebDriver();
        PageFactory.initElements(new LocatorFactory(getDriver()), this);
        this.load();
    }

    /**
     * if this function fails assumes the component is not loaded, trying a second time, if fails again
     * crash, see {@link LoadableComponent#isLoaded()}
     */
    @Override
    protected void isLoaded() {
//        WebElement ele = waitFor(getInitElement());
//        if (ele == null)
//            throw new RuntimeException("Page not loaded");

    }

    /**
     * Syntax sugar to get the driver
     *
     * @return The corresponding web driver for this test
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Wait some time for the given element,
     *
     * @param by By implementation, For shadow root use ByShadow
     * @return return the found element, otherwise null
     */
    protected WebElement waitFor(By by) {
        log.info(by);
        FluentWait<WebDriver> wait = new FluentWait(getDriver());
        wait.withTimeout(GlobalConfig.TIMEOUT).ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(x -> x.findElement(by));

    }

    /**
     * Logic to click some dropdown  of the EC
     *
     * @param ele   , make sure is the parent box of the dropdown
     * @param value the value we will set to the dropdown, DO NOT CHECK if the value given is valid
     */
    protected void dropDown(WebElement ele, String value) {
        clickWaiting(ele);
        WebElement parent = new ShadowQuery().findElement("#lov:not([style=\"display: none;\"])");
        parent = new ShadowQuery().expandDom(parent);
        WebElement input = new ShadowQuery().findElement("#searchbox #input", parent);
        slowWrite(value, input);
        WebElement finalParent = parent;
        clickWaiting(() -> new ShadowQuery().findElement("div[title^=\"" + value + "\"]", finalParent));
    }

    /**
     * Helper method to fill tag fields
     *
     * @param ele   a tag field, selected with something like div.attribute-view-wrapper[title="AAIA Part Type Description"] + div
     * @param value the value, It can be anything
     */
    protected void tagsField(WebElement ele, String value) {
        FluentWait<WebElement> wait = new FluentWait<WebElement>(ele);
        wait.withTimeout(GlobalConfig.TIMEOUT);
        wait.withMessage("Trying to select a tag");
        wait.ignoring(ElementNotInteractableException.class);
        wait.ignoring(ElementNotSelectableException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.until((WebElement t) -> {
            t.click();
            WebElement parent = new ShadowQuery().findByXpath("../..", t);
            parent = new ShadowQuery().shadowParent(parent);
            new ShadowQuery().findElement("#textbox", parent).sendKeys(value + "\n");
            return true;
        });
    }

    /**
     * Helper method to check if value is in a tag field
     *
     * @param ele      div.attribute-view-wrapper[title="AAIA Part Type Description"] + div
     * @param tagValue The expected tag value
     * @return true if the field contains the given tag
     */
    protected boolean tagsFieldContain(WebElement ele, String tagValue) {
        try {
            new ShadowQuery().findElement("[name=\"" + tagValue + "\"]", ele);
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Return the value of the given drop
     *
     * @param ele
     * @return the value, it just retrieves the first one, a drop can have multiple of them
     */
    protected String getDropValue(WebElement ele) {
        return new ShadowQuery().findElement("pebble-tag-item", ele).getAttribute("name");
    }

    /**
     * Some elements get blocked,lagged or disabled this method give them some time to be in a clickable state, then click it
     *
     * @param ele The element we are trying to click
     */
    protected void clickWaiting(WebElement ele) {
        FluentWait<WebElement> wait = new FluentWait<WebElement>(ele);
        wait.withTimeout(GlobalConfig.TIMEOUT);
        wait.withMessage("Can't click element");
        wait.until((WebElement t) -> {
            try {
               // new ShadowQuery().scrollIntoView(ele);
                ele.click();
                return true;
            } catch (ElementNotInteractableException | ElementNotSelectableException | StaleElementReferenceException e) {
                log.debug("Waiting for a click", e);
                return false;
            }
        });
    }

    /**
     * Get a path from a resource folder
     *
     * @param resourceName
     * @return
     */
    public static String getResourcePath(String resourceName) {
        URL url = AbstractBase.class.getClassLoader().getResource(resourceName);
        if (SystemUtils.IS_OS_WINDOWS) {
            return FilenameUtils.normalize(url.getPath()).substring(1);
        }
        return FilenameUtils.normalize(url.getPath());
    }

    /**
     * Some elements get blocked,lagged or disabled this method give them some time to be in a clickable state, then click it
     *
     * @param getEle getEle file
     */
    protected void clickWaiting(Callable<WebElement> getEle) {
        FluentWait<Callable<WebElement>> wait = new FluentWait<Callable<WebElement>>(getEle);
        wait.withTimeout(GlobalConfig.TIMEOUT);
        wait.withMessage("Trying to click element");
        wait.until((Callable<WebElement> t) -> {
            try {
                WebElement ele = null;
                try {
                    ele = t.call();
                } catch (Exception e) {
                    log.trace(e);
                }
                new ShadowQuery().scrollIntoView(ele);
                ele.click();
                return true;
            } catch (ElementNotInteractableException | ElementNotSelectableException | StaleElementReferenceException e) {
                log.debug("Waiting for a click", e);
                return false;
            }
        });
    }

    /**
     * A generic wait, it can be used to see if the element is ready to work
     *
     * @param lamb a lambda expression, it check each thick versus the result of this function
     * @param why  why is this waiting necessary, this parameter is to have more readable code and logs
     */
    protected void waitFor(Function<WebDriver, Boolean> lamb, String why) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver());
        wait.withTimeout(ShadowQuery.TIMEOUT);
        wait.withMessage(why);
        wait.until(lamb);
    }

    /**
     * A generic wait, it can be used to see if the element is ready to work
     *
     * @param lamb a lambda expression, it check each thick versus the result of this function
     * @param ele  Element to check
     * @param why  why is this waiting necessary, this parameter is to have more readable code and logs
     */
    protected void waitFor(Function<WebElement, Boolean> lamb, WebElement ele, String why) {
        FluentWait<WebElement> wait = new FluentWait<WebElement>(ele);
        wait.withTimeout(ShadowQuery.TIMEOUT);
        wait.withMessage(why);
        wait.until(lamb);
    }

    /**
     * Slow write a string on an input, Sometimes AJAX requires some time between key press
     *
     * @param str to write
     * @param ele target element
     */
    public void slowWrite(String str, WebElement ele) {
        log.info("Slow writing a str:" + str);
        FluentWait<WebElement> wait = new FluentWait<WebElement>(ele);
        wait.withTimeout(GlobalConfig.TIMEOUT);
        wait.withMessage("Can't write element" + ele.getText());
        wait.until((WebElement t) -> {
            for (Character c : str.toCharArray()) {
                try {
                    new ShadowQuery().scrollIntoView(ele);
                    ele.sendKeys(c.toString());
                    Thread.sleep(100);
                } catch (ElementNotInteractableException | InterruptedException | ElementNotSelectableException e) {
                    log.debug("Waiting for a write", e);
                    return false;
                }
            }
            return true;
        });


    }

    /**
     * Sleep a moment the application, this function should be used as last resort
     *
     * @param milliseconds time to wait
     * @param why          reason of the sleep
     */
    protected void snooze(int milliseconds, String why) {
        log.info(why);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error(e);
        }

    }

    /**
     * Functional programing to validate an string is ready to be used
     *
     * @param callEle  a function return an string
     * @param expected a function to evaluate the value
     * @return true is the validation could be finished and it is true
     */
    protected boolean checkText(Callable<String> callEle, Function<String, Boolean> expected) {
        FluentWait<Function<String, Boolean>> wait = new FluentWait<Function<String, Boolean>>(expected);
        wait.withTimeout(GlobalConfig.TIMEOUT);
        wait.withMessage("Looking for value: " + expected);
        try {
            wait.until((x) -> {
                try {
                    return x.apply(callEle.call());
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
        return true;

    }

    /**
     * Generic parse of numbers
     *
     * @param callEle a function return an string which be evaluated
     * @param parseTo a function parse to something, its parameter is  value always is valid
     * @return the parsed number, null if was not able to parse
     */
    protected Number parseWaiting(Callable<String> callEle, Function<String, Number> parseTo) {
        if (checkText(callEle, (x) -> NumberUtils.isCreatable(x))) {
            try {
                return parseTo.apply(callEle.call());
            } catch (Exception e) {
                log.error(e);
            }
        }
        return null;
    }

    /**
     * Generate a random uuid for items
     *
     * @return A random str
     */
    public static String genRandomId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}



