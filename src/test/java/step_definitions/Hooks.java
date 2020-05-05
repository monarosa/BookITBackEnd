package step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utilities.DBUtility;
import utilities.Environment;

import static io.restassured.RestAssured.baseURI;

public class Hooks {

    @Before("@api")
    public void setupAPI(){
        baseURI = Environment.BASE_URI;
    }

    @Before("@db")
    public void setupDB(){
        DBUtility.createDBConnection(Environment.DB_HOST, Environment.DB_USERNAME, Environment.DB_PASSWORD);
    }

    @After("@db")
    public void teardownDB(){
        DBUtility.destroy();
    }

}
