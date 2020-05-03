package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import pojos.Room;
import utilities.APIUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;

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


    @When("user sends GET request to {string}")
    public void user_sends_GET_request_to(String string) {
        response = given().
                accept(contentType).
                auth().oauth2(token).
                when().
                get(string).prettyPeek();
    }

    @Then("user should be able to see {int} rooms")
    public void user_should_be_able_to_see_rooms(int expected) {
        List<Object> rooms = response.jsonPath().get();

        Assert.assertEquals( expected, rooms.size());
    }


    @Then("user should be able to see all room names")
    public void user_should_be_able_to_see_all_room_names() {
        List<Room> rooms = response.jsonPath().getList("", Room.class);
//          will not work because list is Unmodifiable
//        so we cannot change order
      //  Collections.sort(rooms);

        for (Room room : rooms) {
            System.out.println(room.getName());
        }

        System.out.println("#########AFTER SORTING##########");
        //create new list to overcome the issue with Unmodifiable list
        List<Room> rooms2 = new ArrayList<>(rooms);

        Collections.sort(rooms2);

        for (Room room : rooms2) {
            System.out.println(room.getName());
        }
    }
    @Then("user payload contains following room names:")
    public void user_payload_contains_following_room_names(List<String> dataTable) {

        List<String> actualRoomNames = response.jsonPath().getList("name");

        Assert.assertTrue(actualRoomNames.containsAll(dataTable));
    }
    @Then("user deletes previously added students")
    public void user_deletes_previously_added_students(List<Map<String, String>> students) {
        for (Map<String, String> student : students) {
            response = APIUtilities.deleteMe(student.get("email"), student.get("password"));
        }
    }

    @When("user sends DELETE request to {string} to exclude student")
    public void user_sends_DELETE_request_to_to_exclude_student(String string) {
        response = given().
                accept(contentType).auth().oauth2(token).
                when().
                delete(string).prettyPeek();
    }

    @When("user verifies that status line contains {string}")
    public void user_verifies_that_status_line_contains(String string) {
        Assert.assertTrue(response.statusLine().contains(string));
    }

    @When("user verifies that payload contains {string} message")
    public void user_verifies_that_payload_contains_message(String string) {
        response.then().assertThat().body("", contains(string));
    }
}
