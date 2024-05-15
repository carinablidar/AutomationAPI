package Tests;

import io.restassured.response.Response;
import objectData.RequestAccount;
import objectData.ResponseAccountSuccess;
import objectData.response.ResponseTokenSuccess;
import objectData.restClient.RestClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import propertiesUtilitty.PropertiesUtilitty;

public class CreateAccountTest {

    public RequestAccount requestAccountBody;
    public String token;
    public String userID;
    RestClient restClient = new RestClient();

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

        //pregatim requestul
        PropertiesUtilitty propertiesUtilitty = new PropertiesUtilitty("Request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertiesUtilitty.getAllData());

        //executam requestul
        restClient.getRequestSpecification().body(requestAccountBody);
        Response response = restClient.getRequestSpecification().post("Account/v1/User");

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

        //executam requestul
        restClient.getRequestSpecification().body(requestAccountBody);
        Response response =  restClient.getRequestSpecification().post("Account/v1/GenerateToken");

        //validam response
        Assert.assertTrue(response.getStatusLine().contains("200"));
        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);
        token = responseTokenSuccess.getToken();

        Assert.assertEquals(responseTokenSuccess.getStatus(), "Success");
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");
    }

    public void checkAccountPresence() {

        //ne autorizam pe baza la token
        restClient.getRequestSpecification().header("Authorization", "Bearer "+token);

        //executam requestul
        Response response = restClient.getRequestSpecification().get("Account/v1/User"+userID);

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

        // ne autorizam pe baza la token
        restClient.getRequestSpecification().header("Authorization","Bearer " + token);

        // executam request-ul
        Response response = restClient.getRequestSpecification().delete("/Account/v1/User/" + userID);
        System.out.println(response.getStatusLine());

        Assert.assertTrue(response.getStatusLine().contains("204"));
        Assert.assertTrue(response.getStatusLine().contains("No Content"));
    }
}
