package Service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import objectData.restClient.RestClient;

public class CommonService {

    public Response post(Object requestBody, String endPoint) {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(requestBody);
        return new RestClient().performRequest("post", requestSpecification, endPoint);
    }

    public Response get(String token, String endPoint) {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Authorization","Bearer " + token);
        return new RestClient().performRequest("get", requestSpecification, endPoint);
    }

    public Response delete(String token, String endPoint) {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Authorization","Bearer " + token);
        return new RestClient().performRequest("delete", requestSpecification, endPoint);
    }

    public Response post(String token, Object requestBody, String endPoint) {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.body(requestBody);
        requestSpecification.header("Authorization","Bearer " + token);
        return new RestClient().performRequest("post", requestSpecification, endPoint);
    }
}