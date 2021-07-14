package stepdefinition;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class stepDefinitionLogin {
	
	private static final String USER_ID = "bae12a24-e721-4720-b4e0-d3505e132c44";
	private static final String USERNAME = "ShivaDh";
	private static final String PASSWORD = "Test@123";
	private static final String BASE_URL = "https://bookstore.toolsqa.com";

	private static String token;
	private static Response response;
	private static String jsonString;
	private static String bookId;


	@Given("I am an authorized user")
	public void iAmAnAuthorizedUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");
		response = request.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}")
				.post("/Account/v1/GenerateToken");

		String jsonString = response.asString();
		System.out.println(jsonString);
		token = JsonPath.from(jsonString).get("token");

	}

	@Given("A list of books are available")
	public void listOfBooksAreAvailable() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		response = request.get("/BookStore/v1/Books");

		jsonString = response.asString();
		List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
		Assert.assertTrue(books.size() > 0);

		bookId = books.get(0).get("isbn");	   
	}

	@When("I add a book to my reading list")
	public void addBookInList() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"userId\": \"" + USER_ID + "\", " +
				"\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}")
				.post("/BookStore/v1/Books");
		String jsonString = response.asString();
		System.out.println(jsonString);
	}

	@Then("The book is added")
	public void bookIsAdded() {
		Assert.assertEquals(201, response.getStatusCode());
	}

	@When("I remove a book from my reading list")
	public void removeBookFromList() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + USER_ID + "\"}")
				.delete("/BookStore/v1/Book");


	}

	@Then("The book is removed")
	public void bookIsRemoved() {
		Assert.assertEquals(204, response.getStatusCode());

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.get("/Account/v1/User/" + USER_ID);
		Assert.assertEquals(200, response.getStatusCode());

		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
		Assert.assertEquals(0, booksOfUser.size());
	}

	
	

    @Given("User is on landing page")
    public void user_is_on_landing_page() throws Throwable {
    	System.out.println("given");
    }

    @When("User logs into application with {string} and {string}")
    public void user_logs_into_application(String strArg1, String strArg2) throws Throwable {
    	System.out.println("when"+ strArg1 + "  " + strArg2);
    }
    
    @When("User logs in with")
    public void User_logs_in_with(DataTable dt) throws Throwable {
    	List<List<String>> ls = dt.asLists();
    	System.out.println("when"+ ls.get(0).get(0) + "  " + ls.get(0).get(1));
    }
    
    @When("^logs into application with (.+) and (.+)$")
    public void user_logs_into_application_with_and(String username, String password) throws Throwable {
    	System.out.println(username+" "+password);
    }

    @Then("Home page is populated")
    public void home_page_is_populated() throws Throwable {
    	System.out.println("Then");
    }

    @And("Cards are displayed")
    public void cards_are_displayed() throws Throwable {
    	System.out.println("And");
    }

}