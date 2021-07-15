package stepdefinition;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;

import blacklist.BlackListScenarioContext;
import helper.ServiceRequestHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class StepDefinitionBLackList {
	private static Properties properties;
	private static String BASE_URL;
	private static String BL_URI;
	private static String TS_URI;

	static {
		try {
			ClassLoader cLoader = StepDefinitionBLackList.class.getClassLoader();
			System.out.println(Arrays.toString(((URLClassLoader) cLoader).getURLs()));
			properties = new Properties();
			InputStream inputStream = cLoader.getResourceAsStream("blacklist.properties");
			properties.load(inputStream);
			BASE_URL = properties.getProperty("BASE_URL");
			BL_URI = properties.getProperty("BL_URI");
			TS_URI = properties.getProperty("TS_URI");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	BlackListScenarioContext scenarioContext;

	public StepDefinitionBLackList(BlackListScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@Given("I am a new user and my {string}, {string} exists in your company blacklist")
	public void i_am_a_new_user_and_my_info_is_in_the_blacklist(String userinfotype, String info) {
		scenarioContext.setScenarioContext(userinfotype, info);
		Integer id = ServiceRequestHelper.addToblacklist(userinfotype, info, BASE_URL, BL_URI);
		Assert.assertNotNull("Failed to add test data - " + info, id);
		if (id != 0) {
			scenarioContext.setBlackListItemId(id);
		}
	}

	@Given("I am a new user and my {string}, {string} does not exist in your company blacklist")
	public void i_am_a_new_user_and_my_info_does_not_exist_in_the_blacklist(String userinfotype, String info) {
		scenarioContext.setScenarioContext(userinfotype, info);
	}

	@Given("I am a fraud analyst with auth token {string}")
	public void i_am_a_fraud_analyst_with_auth_token(String token) {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given().contentType("application/json").queryParam("token", token);
		ValidatableResponse response = request.log().all().get(TS_URI).then().log().all();
		List<Integer> ids = response.extract().jsonPath().get("id");
		if (ids == null || ids.size() <= 0) {
			scenarioContext.setPermission(false);
		}
	}

	@When("I sign up using my {string}")
	public void i_sign_up_with_info(String info) {

	}

	@When("I add {string}, {string} in the company blacklist")
	public void i_add_frauduserifo_in_the_company_blacklist(String userinfotype, String info) {
		if (scenarioContext.getPermission()) {
			scenarioContext.setScenarioContext(userinfotype, info);

			Integer id = ServiceRequestHelper.addToblacklist(userinfotype, info, BASE_URL, BL_URI);
			scenarioContext.setBlackListItemId(id);
		}
	}

	@Then("you check it against your company blacklist")
	public void check_it_against_the_company_blacklist() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given().contentType("application/json")
				.queryParam(scenarioContext.getUserinfotype(), scenarioContext.getUserinfo());
		Response response = request.log().all().get(BL_URI);
		List<String> codes = response.jsonPath().get("code");
		if (codes == null || codes.size() == 0) {
			scenarioContext.setCode("bl_approve");
		} else {
			scenarioContext.setCode(codes.get(0));
		}

	}

	@Then("Status code {string} is returned")
	public void status_code_is_returned(String code) {
		Assert.assertEquals(code, scenarioContext.getCode());
	}

	@Then("Item is succefully created")
	public void item_created_successfully() {
		Assert.assertNotNull("Null id returned even after response had Status code 201",
				scenarioContext.getBlackListItemId());
	}

	@Then("Permission is denied")
	public void permission_is_denied() {
		Assert.assertFalse(scenarioContext.getPermission());
	}

}