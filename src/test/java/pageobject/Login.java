package pageobject;

import driver.SharedDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import runner.GlobalConfig;

/**
 * Class for page loaded when navigating to main page
 */
public class Login extends AbstractBase<Login> {
    private static final Log log = LogFactory.getLog(Login.class);

    @FindBy(name = "q")
    private WebElement userNameBox;

    @FindBy(name = "q")
    private WebElement search;
    @FindBy(name = "btnK")
    private WebElement searchButton;



    public Login(SharedDriver sharedDriver) {
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
        return By.name("q");
    }

    public void search(String value){
        search.sendKeys(value);
      //  search.sendKeys(Keys.ESCAPE);
        log.info("Search Key Entered as"+value);
    }

    public void googleSearch(){
        searchButton.click();
        log.info("CLicked Google Search ");

    }

}


