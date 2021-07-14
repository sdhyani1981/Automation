package hooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;

import blacklist.BlackListScenarioContext;
import helper.ServiceRequestHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BlackListHooks {
	private static String BL_ANALYST_VALID_TOKEN;
	private static Properties properties;
	private static String BASE_URL;
	private static String BL_URI;
	private static String TS_URI;

	static {
		try {
			properties = new Properties();
			File file = new File(
					"//Users//shivanidhyani//eclipse-workspace//Automation//src//main//java//config//blacklist.properties");
			InputStream inputStream = new FileInputStream(file);
			properties.load(inputStream);
			BASE_URL = properties.getProperty("BASE_URL");
			BL_URI = properties.getProperty("BL_URI");
			TS_URI = properties.getProperty("TS_URI");
			BL_ANALYST_VALID_TOKEN = properties.getProperty("BL_ANALYST_VALID_TOKEN");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	BlackListScenarioContext scenarioContext;

	public BlackListHooks(BlackListScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@Before("@bl_auth")
	public void setUpAuth() {
		Integer id = ServiceRequestHelper.addAnalystToken(BL_ANALYST_VALID_TOKEN, BASE_URL, TS_URI);
		scenarioContext.setAnalystId(id);
	}

	@After("@bl_auth")
	public void tearDownAuth() {
		Integer id = scenarioContext.getAnalystId();
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		Response response = request.log().all().delete(TS_URI + "/" + id);
		Assert.assertEquals("Failed to Delete test data", 200, response.getStatusCode());
	
	}

	@After("@bl_check or @bl_auth")
	public void tearDown() {
		Integer id = scenarioContext.getBlackListItemId();
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		Response response = request.log().all().delete(BL_URI + "/" + id);
		Assert.assertEquals("Failed to Delete test data", 200, response.getStatusCode());	
	}

}
