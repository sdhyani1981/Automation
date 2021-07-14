package helper;

import org.json.JSONObject;
import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ServiceRequestHelper {

	public static RequestSpecification createBlackListRequest(String userinfotype, String info, String code) {
		RequestSpecification request = RestAssured.given().contentType("application/json");
		JSONObject requestParams = new JSONObject();
		requestParams.put(userinfotype, info);
		requestParams.put("code", code);
		return request.body(requestParams.toString());
	}

	public static Integer addToblacklist(String userinfotype, String info, String BASE_URL, String BL_URI) {
		String code = "bl_" + userinfotype;
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = createBlackListRequest(userinfotype, info, code);
		ValidatableResponse response = request.log().all().post(BL_URI).then().log().all();
		Assert.assertNotNull("Add to blacklist response came back as NULL for " + info, response);
		Assert.assertEquals(201, response.extract().statusCode());
		return response.extract().jsonPath().get("id");
	}

	public static Integer addAnalystToken(String token, String BASE_URL, String TS_URI) {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given().contentType("application/json");
		JSONObject requestParams = new JSONObject();
		requestParams.put("token", token);
		request.body(requestParams.toString());
		ValidatableResponse response = request.log().all().post(TS_URI).then().log().all();
		Assert.assertNotNull("Add to token service response came back as NULL for " + token, response);
		Assert.assertEquals(201, response.extract().statusCode());
		Integer id = (Integer) response.extract().jsonPath().get("id");
		Assert.assertNotNull(token + " - Null id returned even after response had Status code 201", id);
		return id;
	}
}
