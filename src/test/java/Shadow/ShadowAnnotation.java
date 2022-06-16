package Shadow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;

/**
 * Layer for the selenium pipeline, We are interested to use our custom ByShadow
 */
public class ShadowAnnotation extends Annotations {
    private Field field;

    public ShadowAnnotation(Field field) {
        super(field);
        this.field = field;

    }

    @Override
    public By buildBy() {
        FindByShadow findByShadow = field.getAnnotation(FindByShadow.class);
        if (findByShadow == null) {
            return super.buildByFromDefault();
        }
        return new ByShadow(findByShadow.selector());
    }
}
