package blacklist;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/feature", monochrome= true,
		glue = {"stepdefinition","hooks"}, tags = "@blacklist",
				plugin= {"json:target/cucumber.json" }
		)
public class TestRunner {

}
