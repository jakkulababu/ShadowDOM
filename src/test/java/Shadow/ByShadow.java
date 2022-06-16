package Shadow;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;

/**
 * A new Selenium by, Using shadow root
 */
public class ByShadow extends By implements Serializable {
    private final String shadowSelector;

    public ByShadow(String shadowSelector) {
        if (shadowSelector == null) {
            throw new IllegalArgumentException("Cannot find elements when the selector is null");
        } else {
            this.shadowSelector = shadowSelector;
        }
    }

    public List<WebElement> findElements(SearchContext context) {
        return new ShadowQuery().findElements(this.shadowSelector);
    }

    public String toString() {
        return "By.cssSelector: " + this.shadowSelector;
    }
}
