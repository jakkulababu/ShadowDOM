package runner;

import io.cucumber.testng.CucumberOptions;

/**
 * Starting point of the test, here you can implement plugins or options for cucumber, if you are tinkering here
 * READ CUCUMBER DOCS
 */
@CucumberOptions(glue = "stepdef",
        features = "src/test/resources/features/",
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "json:target/cucumber-report.json"},
        tags = {"@smoke"}
)
public class RunnerIT extends AbstractTestNGCucumberParallelTests {


}

