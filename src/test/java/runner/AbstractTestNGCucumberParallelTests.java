package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.DataProvider;

/**
 * This place is for internal config of the framework, If you are tinkering here, READ THE CORRESPONDING TESTNG DOCS
 */
public abstract class AbstractTestNGCucumberParallelTests extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {
		return super.scenarios();
	}

}