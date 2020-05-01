package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import utilities.APIUtilities;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIStepDefinitions {
    private String token;
    private ContentType contentType;
    private Response response;


    @Given("authorization token is provided for {string}")
    public void authorization_token_is_provided_for(String role) {
        token = APIUtilities.getToken(role);
    }

    @Given("user accepts content type as {string}")
    public void user_accepts_content_type_as(String string) {
        if (string.toLowerCase().contains("json")) {
            contentType = ContentType.JSON;
        } else if (string.toLowerCase().contains("xml")) {
            contentType = ContentType.XML;
        }
    }

    /**
     * Any number in cucumber test step, becomes step definition  (variable)
     * By changing this number, you are not changing a context of test step
     */


    @When("user sends POST request to {string} with following information:")
    public void user_sends_POST_request_to_with_following_information(String path, List<Map<String, String>> students) {
        for (Map<String, String> student : students) {
            response = given().
                    auth().oauth2(token).
                    queryParams(student).
                    accept(contentType).
                    when().
                    post(path);

            response.then().log().body(true);

        }
    }

    // And user verifies that response status code is 200
    @Then("user verifies that response status code is {int}")
    public void user_verifies_that_response_status_code_is(int expected) {
        Assert.assertEquals(expected, response.statusCode());
    }


}
