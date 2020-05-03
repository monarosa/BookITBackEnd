package utilities;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class APIUtilities implements Endpoints{

    private static String URI = Environment.BASE_URI;

    public static String getToken(String role) {
        String userName = "";
        String password = "";
        if (role.toLowerCase().contains("lead")) {
            userName = Environment.LEADER_USERNAME;
            password = Environment.LEADER_PASSWORD;
        } else if (role.toLowerCase().contains("teacher")) {
            userName = Environment.TEACHER_USERNAME;
            password = Environment.TEACHER_PASSWORD;
        } else if (role.toLowerCase().contains("member")) {
            userName = Environment.MEMBER_USERNAME;
            password = Environment.MEMBER_PASSWORD;
        } else {
            throw new RuntimeException("Invalid user type!");
        }
        Response response = given().
                queryParam("email", userName).
                queryParam("password", password).
                when().
                get("/sign").prettyPeek();
        return response.jsonPath().getString("accessToken");

    }

    /**
     * Delete user based on email and password
     *
     * @param email
     * @param password
     * @return response
     */
    public static Response deleteMe(String email, String password) {
        String token = given().
                queryParam("email", email).
                queryParam("password", password).
                when().
                get("/sign").prettyPeek().jsonPath().getString("accessToken");

        int userToDelete = given().auth().oauth2(token).
                when().
                get("/api/users/me").jsonPath().getInt("id");

        Response response = given().auth().oauth2(getToken("teacher")).delete(DELETE_STUDENT, userToDelete);
        response.prettyPeek();
        return response;


    }







}
