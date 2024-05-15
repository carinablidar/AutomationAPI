package objectData.restClient;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {


    public Response performRequest(String requestType, RequestSpecification requestSpecification, String endPoint) {
        switch (requestType) {
            case "post":
                return prepareClient(requestSpecification).post(endPoint);
            case "get":
                return prepareClient(requestSpecification).get(endPoint);
            case "delete":
                return prepareClient(requestSpecification).delete(endPoint);
        }
        return null;
    }

    public RequestSpecification prepareClient(RequestSpecification requestSpecification) {
        requestSpecification.baseUri("https://demoqa.com/");
        requestSpecification.contentType("application/json");

        return requestSpecification;
    }
}
