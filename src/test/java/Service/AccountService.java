package Service;

import io.restassured.response.Response;
import objectData.RequestAccount;
import objectData.ResponseAccountSuccess;
import objectData.response.ResponseTokenSuccess;
import objectData.restClient.RestEndPoint;
import objectData.restClient.RestStatus;
import org.testng.Assert;

public class AccountService extends CommonService {

    public ResponseAccountSuccess createAccount(RequestAccount requestBody) {

        Response response = post(requestBody, RestEndPoint.ACCOUNT_USER);

        System.out.println(response.getStatusLine());

        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_201));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_CREATED));

        ResponseAccountSuccess responseAccountSuccess = response.body().as(ResponseAccountSuccess.class);

        Assert.assertTrue(responseAccountSuccess.getUsername().equals(requestBody.getUserName()));

        return  responseAccountSuccess;
    }

    public ResponseTokenSuccess generateToken(RequestAccount requestAccount) {

        Response response = post(requestAccount, RestEndPoint.ACCOUNT_TOKEN);

        System.out.println(response.getStatusLine());

        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_200));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_OK));

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);

        Assert.assertEquals(responseTokenSuccess.getStatus(), "Success");
        Assert.assertEquals(responseTokenSuccess.getResult(), "User authorized successfully.");

        return  responseTokenSuccess;
    }

    public void checkAccountPresence(String userID, String token) {

        Response response = get(token, RestEndPoint.ACCOUNT_GET_USER+userID);

        System.out.println(response.getStatusLine());

        if(response.getStatusLine().contains(RestStatus.SC_200)) {
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_200));
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_OK));
        }
        else {
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_401));
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_UNAUTHORIZED));
        }
    }

    public void deleteAccount(String userID, String token) {

        Response response = delete(token, RestEndPoint.ACCOUNT_DELETE_USER + userID);
        System.out.println(response.getStatusLine());

        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_204));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_NOCONTENT));
    }
}
