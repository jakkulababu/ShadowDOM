package stepdef;

import driver.SharedDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobject.ShadowDemoPage;


public class ShadowDemo {
    private SharedDriver driver;
    //first init of the shared data on the whole test
    private final ThreadSafeShared sharedData;
    private ShadowDemoPage shadowDemoPage;


    public ShadowDemo(ThreadSafeShared sharedData, ShadowDemoPage shadowDemoPage) {
        this.sharedData = sharedData;
        this.shadowDemoPage = shadowDemoPage;
    }


  

    @Given("user click switchButton")
    public void goToEnterpriseCatalogPage() {
    	shadowDemoPage = (ShadowDemoPage) shadowDemoPage.get();
    	shadowDemoPage.clickSwitch();
    }
    
    
}
