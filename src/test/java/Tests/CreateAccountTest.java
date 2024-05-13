package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import objectData.RequestAccount;
import objectData.ResponseAccountSuccess;
import objectData.response.ResponseTokenSuccess;
import org.testng.Assert;
import org.testng.annotations.Test;
import propertiesUtilitty.PropertiesUtilitty;

public class CreateAccountTest {

    public RequestAccount requestAccountBody;
    public String token;
    public String userID;

    @Test
    public void testMethod() {
        System.out.println("=== STEP 1: CREATE NEW ACCOUNT ===");
        createAccount();

        System.out.println("=== STEP 2: GENERATE TOKEN ===");
        generateToken();

        System.out.println("=== STEP 3: CHECK ACCOUNT ===");
        checkAccountPresence();

        System.out.println("=== STEP 4: DELETE ACCOUNT ===");
        deleteUser();

        System.out.println("===STEP 5: RECHECK ACCOUNT===");
        checkAccountPresence();
    }

    public void createAccount() {
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com/");
        requestSpecification.contentType("application/json");

        //pregatim requestul
        PropertiesUtilitty propertiesUtilitty = new PropertiesUtilitty("Request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertiesUtilitty.getAllData());

        //executam requestul
        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post("Account/v1/User");

        //validam response
        Assert.assertTrue(response.getStatusLine().contains("201"));
        Assert.assertTrue(response.getStatusLine().contains("Created"));

        ResponseAccountSuccess responseAccountSuccess = response.body().as(ResponseAccountSuccess.class);
        userID = responseAccountSuccess.getUserId();

        //responseBody.prettyPrint();
        Assert.assertTrue(responseAccountSuccess.getUsername().equals(requestAccountBody.getUserName()));
        System.out.println(responseAccountSuccess.getUserId());
    }

    public void generateToken() {
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com/");
        requestSpecification.contentType("application/json");

        //executam requestul
        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post("Account/v1/GenerateToken");

        //validam response
        Assert.assertTrue(response.getStatusLine().contains("200"));
        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);
        token = responseTokenSuccess.getToken();

        Assert.assertEquals(responseTokenSuccess.getStatus(), "Success");
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");
    }

    public void checkAccountPresence() {
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com/");
        requestSpecification.contentType("application/json");

        //ne autorizam pe baza la token
        requestSpecification.header("Authorization", "Bearer "+token);

        //executam requestul
        Response response = requestSpecification.get("Account/v1/User"+userID);

        System.out.println(response.getStatusLine());

        if(response.getStatusLine().contains("200")) {
            Assert.assertTrue(response.getStatusLine().contains("200"));
            Assert.assertTrue(response.getStatusLine().contains("OK"));
        }
        else {
            Assert.assertTrue(response.getStatusLine().contains("401"));
            Assert.assertTrue(response.getStatusLine().contains("Unauthorized"));
        }
    }

    public void deleteUser() {

        // configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        // ne autorizam pe baza la token
        requestSpecification.header("Authorization","Bearer " + token);

        // executam request-ul
        Response response = requestSpecification.delete("/Account/v1/User/" + userID);
        System.out.println(response.getStatusLine());

        Assert.assertTrue(response.getStatusLine().contains("204"));
        Assert.assertTrue(response.getStatusLine().contains("No Content"));

    }
}
