package pageobject;

import driver.SharedDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Shadow.FindByShadow;
import runner.GlobalConfig;

/**
 * Class for page loaded when navigating to main page
 */
public class ShadowDemoPage extends AbstractBase<ShadowDemoPage> {
    private static final Log log = LogFactory.getLog(ShadowDemoPage.class);

    @FindBy(name = "q")
    private WebElement userNameBox;

    @FindBy(name = "q")
    private WebElement search;
    @FindBy(name = "btnK")
    private WebElement searchButton;
    
   @FindByShadow (selector="#basic-switch")
   private WebElement switchbutton;



    public ShadowDemoPage(SharedDriver sharedDriver) {
        super(sharedDriver);

    }


    @Override
    protected void load() {
        getDriver().get(GlobalConfig.URL);
    }

    @Override
    protected void isLoaded() throws Error {

    }

    @Override
    protected By getInitElement() {
        return By.name("basic-switch");
    }

   

    
    public void clickSwitch() {
    	clickWaiting(switchbutton);
    }
    
}


