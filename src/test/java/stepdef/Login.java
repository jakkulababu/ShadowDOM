package stepdef;

import driver.SharedDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class Login {
    private SharedDriver driver;
    //first init of the shared data on the whole test
    private final ThreadSafeShared sharedData;
    private pageobject.Login loginPage;


    public Login(ThreadSafeShared sharedData, pageobject.Login login) {
        this.sharedData = sharedData;
        this.loginPage = login;
    }


    @When("User Search {string}")
    public void userSearch(String arg0) {
        //loginPage = (pageobject.Login) loginPage.get();
        loginPage.search(arg0);
    }

    @And("I click Google Search")
    public void iClickGoogleSearch() {
        loginPage.googleSearch();
    }

    @Given("Go to Google page")
    public void goToEnterpriseCatalogPage() {
        loginPage = (pageobject.Login) loginPage.get();
    }
}
