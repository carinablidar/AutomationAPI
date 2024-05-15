package Tests;

import Service.AccountService;
import objectData.RequestAccount;
import objectData.ResponseAccountSuccess;
import objectData.response.ResponseTokenSuccess;
import objectData.restClient.RestClient;
import org.testng.annotations.Test;
import propertiesUtilitty.PropertiesUtilitty;

public class CreateAccountTest {

    public RequestAccount requestAccountBody;
    public String token;
    public String userID;
    public AccountService accountService;
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

        System.out.println("=== STEP 5: RECHECK ACCOUNT ===");
        checkAccountPresence();
    }

    public void createAccount() {

        //pregatim requestul
        PropertiesUtilitty propertiesUtilitty = new PropertiesUtilitty("Request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertiesUtilitty.getAllData());

        accountService = new AccountService();

        ResponseAccountSuccess responseAccountSuccess = accountService.createAccount(requestAccountBody);
        userID = responseAccountSuccess.getUserId();
    }

    public void generateToken() {

        ResponseTokenSuccess responseTokenSuccess = accountService.generateToken(requestAccountBody);
        token = responseTokenSuccess.getToken();
    }

    public void checkAccountPresence() {

      accountService.checkAccountPresence(userID, token);
    }

    public void deleteUser() {
        accountService.deleteAccount(userID, token);
    }
}
