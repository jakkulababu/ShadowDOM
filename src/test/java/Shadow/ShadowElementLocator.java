package Shadow;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

/**
 * Layer for the selenium pipeline, We are interested to use our custom ByShadow
 */
public class ShadowElementLocator implements ElementLocator {
    private final ShadowAnnotation shadowAnnotation;

    public ShadowElementLocator(SearchContext searchContext, ShadowAnnotation shadowAnnotation) {
        this.shadowAnnotation=shadowAnnotation;
    }

    @Override
    public WebElement findElement() {
        return shadowAnnotation.buildBy().findElement(null);
    }

    @Override
    public List<WebElement> findElements() {
        return shadowAnnotation.buildBy().findElements(null);
    }
}
