package step_definitions;

import io.cucumber.java.Before;
import utilities.Environment;

import static io.restassured.RestAssured.baseURI;

public class Hooks {

    @Before("@api")
    public void setupAPI(){
        baseURI = Environment.BASE_URI;
    }
}
