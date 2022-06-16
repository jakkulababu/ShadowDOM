package Shadow;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

/**
 * Overriding of the default factory, We need to add our shadow root locator
 */
public class LocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;

    public LocatorFactory(SearchContext context) {
        this.searchContext = context;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        FindByShadow annotation = field.getAnnotation(FindByShadow.class);

        if (annotation == null) {
            // if our annotation is not present give it to selenium's defaults
            return new DefaultElementLocator(this.searchContext, new Annotations(field));
        }
        return new ShadowElementLocator(this.searchContext, new ShadowAnnotation(field));
    }
}
