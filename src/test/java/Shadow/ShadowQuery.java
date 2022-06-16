package Shadow;


import driver.DriverFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import runner.GlobalConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to work with the shadow Dom web standard
 * https://developer.mozilla.org/en-US/docs/Web/Web_Components/Using_shadow_DOM
 * TLDR; some web apps use Shadow Dom to encapsulate logic, but this brakes traditional methods of searching
 * inside of a web page
 */
public class ShadowQuery {

    private static final Log log = LogFactory.getLog(ShadowQuery.class);
    public static final Duration TIMEOUT = GlobalConfig.TIMEOUT;
    private static final Duration POLLING = GlobalConfig.POLLING;
    private final WebDriver driver;

    /**
     * Insert the javacript helper function;
     */
    public ShadowQuery() {
        this.driver = DriverFactory.getDriver();
        reload();
    }

    /**
     * This method injects some javascript code inside of the web page which is been tested, if the page redirects or reload
     * it need to be called again
     */
    public void reload() {
        String funcJS;
        funcJS = (new BufferedReader(new InputStreamReader(ShadowQuery.class.getResourceAsStream("/finder.js")))).lines()
                .collect(Collectors.joining());
        runJS(getBody(), funcJS);

    }

    /**
     * Find an element using js xpath selector
     *
     * @param xpathSelector the xpath selector
     * @param ele           the root element
     * @return the found element
     */
    public WebElement findByXpath(String xpathSelector, WebElement ele) {
        return (WebElement) runJS(ele, "return azElementByXpath('" + xpathSelector + "',arguments[0]);");
    }

    /**
     * Helper function that query the body of the whole page
     *
     * @return the body of the page.
     */
    public WebElement getBody() {
        return driver.findElement(By.tagName("body"));
    }

    /**
     * Query some element directly to the web page using javascript, it returns the first coincidence that finds,
     * there for if the page has mulitple shadow DOMs with the same id, It will just return the first one,
     *
     * @param selector it is a standard CSS selector. however is not possible "shadowdom div",
     *                 it has to be a selector that works in a single DOM
     * @return A list of WebElements
     */
    public List<WebElement> queryJS(String selector) {
        return queryJS(selector, getBody());
    }

    /**
     * Bring the element to the view port
     *
     * @param ele can be null
     */
    public void scrollIntoView(WebElement ele) {
        runJS(ele, "arguments[0].scrollIntoView()");
    }

    /**
     * Search the Dom recursively expanding shadow DOMs until finds the first element that CSS evaluates correct
     *
     * @param selector an css selector
     * @param root     The starting container
     * @return a list of WebElements it can be empty
     */
    @SuppressWarnings("unchecked")
    public List<WebElement> queryJS(String selector, WebElement root) {
        return (List<WebElement>) runJS(root,
                String.format("return window.searchAZShadow(arguments[0],'%s');", selector));
    }

    /**
     * Expand a single node it is inner shadow DOM, do not check its existence, there for can trigger a runtime exception
     *
     * @param ele element to expand
     * @return the new dom ready to look into it
     */
    private WebElement expandDomJS(WebElement ele) {
        return (WebElement) runJS(ele, "console.log(arguments[0]);  return arguments[0].shadowRoot");
    }

    /**
     * Javascript sometimes takes time to populate the DOM, this function retry for x number the seconds to expand it
     *
     * @param ele to expand
     * @return An expanded element
     */
    public WebElement expandDom(WebElement ele) {
        FluentWait<WebElement> wait = new FluentWait<>(ele);
        wait.withTimeout(TIMEOUT);
        wait.ignoring(TimeoutException.class);
        wait.withMessage("Error expanding element" + ele);
        final WebElement[] found = new WebElement[1];
        wait.until((WebElement t) -> {
            try {
                found[0] = expandDomJS(ele);
                return found[0] != null;
            } catch (NoSuchElementException e) {
                log.debug("Can't find element, retying", e);
                return false;
            }
        });
        return found[0];
    }

    /**
     * Get the parent of a shadowRoot node
     *
     * @param ele shadowRoot node
     * @return parent of the shadowRoot
     */
    public WebElement shadowParent(WebElement ele) {
        return (WebElement) runJS(ele, "return arguments[0].host");
    }

    /**
     * Run some random JS on the browser, NOTE it can cause a runtime exception
     *
     * @param ele element as parameter, is optional
     * @param js  the instructions to run
     * @return it can return whatever the parameter of js returns
     */
    public Object runJS(WebElement ele, String js) {
        return ((JavascriptExecutor) this.driver).executeScript(js, ele);
    }

    /**
     * Does the same thing that {@link ShadowQuery#queryJS(String)} but also waits some x seconds retrying to get a non
     * null value, if it fails throws an exception
     *
     * @param selector {@link ShadowQuery#queryJS(String)}
     * @param root     {@link ShadowQuery#queryJS(String)}
     * @return {@link ShadowQuery#queryJS(String)}
     */
    public List<WebElement> findElements(String selector, WebElement root) {
        FluentWait<WebElement> wait = new FluentWait<>(driver.findElement(By.tagName("body")));
        wait.withTimeout(TIMEOUT);
        wait.pollingEvery(POLLING);
        wait.withMessage("Finding element: " + selector);
        @SuppressWarnings("unchecked") final List<WebElement>[] found = new List[1];
        wait.until((WebElement t) -> {
            try {
                found[0] = this.queryJS(selector, root);
                return (found[0].get(0) != null);
            } catch (NullPointerException | ElementNotInteractableException e) {
                log.debug("Can't find element, retying", e);
                return false;
            }
        });
        return found[0];
    }

    /**
     * Does the same thing that {@link ShadowQuery#queryJS(String)} but also waits some x seconds retrying to get a non
     * null value, if it fails throws an exception
     *
     * @param selector {@link ShadowQuery#queryJS(String)}
     * @return {@link ShadowQuery#queryJS(String)}
     */
    public List<WebElement> findElements(String selector) {
        return this.findElements(selector, getBody());
    }

    /**
     * Does the same thing that {@link ShadowQuery#queryJS(String)} but also waits some x seconds retrying to get a non
     * null value, if it fails throws an exception
     *
     * @param selector {@link ShadowQuery#queryJS(String)}
     * @return {@link ShadowQuery#queryJS(String)}
     */
    public WebElement findElement(String selector) {
        return this.findElements(selector, getBody()).get(0);
    }

    /**
     * Does the same thing that {@link ShadowQuery#queryJS(String)} but also waits some x seconds retrying to get a non
     * null value, if it fails throws an exception
     *
     * @param selector {@link ShadowQuery#queryJS(String)}
     * @param root     {@link ShadowQuery#queryJS(String)}
     * @return {@link ShadowQuery#queryJS(String)}
     */
    public WebElement findElement(String selector, WebElement root) {
        return this.findElements(selector, root).get(0);
    }

    public String getCurrentURL() {
        return (String) runJS(getBody(), "return window.location.href");
    }
}
