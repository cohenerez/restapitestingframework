package com.erez.restfull.apitestingframework.coreapitest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.List;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import com.erez.restfull.apitestingframework.verbs.HttpVerbs;
import com.erez.restfull.apitestingframework.verbs.VerbsValidator;
import com.erez.restfull.apitestingframework.utils.IoHelper;

import java.util.List;

public class ApiTest  implements  ApiService{

    RequestSpecification reqSpec;
    HttpVerbs method;
    String url;
    Response resp;



    @Override
    public void init(String url, HttpVerbs method) {
        this.url = url;
        this.method = method;
        reqSpec = given();
    }

    public void initBase(String baseConst) {
        IoHelper  ioHelper = new IoHelper();
        ioHelper.setPath("src/main/resources/Constants.properties");
        try {
            RestAssured.baseURI = ioHelper.loadProperties(baseConst);
        }catch (Exception e) {
            e.printStackTrace();
        }
        reqSpec = RestAssured.given();
    }

    @Override
    public void setHeader(String[][] header) {
        for (int row = 0; row < header.length; row++)
            for (int col = 0; col < header[row].length; col++)
                reqSpec.header(header[row][col], header[row][col + 1]);

    }

    @Override
    public void setHeader(String header, String val) {
        reqSpec.header(header, val);

    }

    @Override
    public void setBody(String body) {
        reqSpec.body(body);

    }

    @Override
    public void setFormParam(String key, String val) {
        reqSpec.formParam(key, val);
    }

    @Override
    public void setQueryParam(String key, String val) {
        reqSpec.queryParam(key, val);
    }

    @Override
    public String callIt() {
       String verb = method.toString().toLowerCase();
       switch(verb){

           case "get":
               resp = reqSpec.get(url);
                break;

           case "patch":
               resp = reqSpec.patch(url);
                break;

           case "post":
               resp = reqSpec.post(url);
               break;

            case "put":
               resp = reqSpec.put(url);
               break;
           case "delete":
               resp = reqSpec.delete(url);
               break;
            default:
               return "invalid method set for API";

       }
        return resp.asString();

    }

    @Override
    public ApiTest assertIt(int code) {
        resp.then().statusCode(code);
        return this;
    }

    @Override
    public ApiTest assertIt(String key, Object val, VerbsValidator operation) {

        switch (operation.toString()) {
            case "EQUALS":
                resp.then().body(key, equalTo(val));
                break;

            case "KEY_PRESENTS":
                resp.then().body(key, hasKey(key));
                break;

            case "HAS_ALL":
                break;

            case "NOT_EQUALS":
                resp.then().body(key, not(equalTo(val)));
                break;

            case "EMPTY":
                resp.then().body(key, empty());
                break;

            case "NOT_EMPTY":
                resp.then().body(key, not(emptyArray()));
                break;

            case "NOT_NULL":
                resp.then().body(key, notNullValue());
                break;

            case "HAS_STRING":
                resp.then().body(key, containsString((String)val));
                break;

            case "SIZE":
                resp.then().body(key, hasSize((int)val));
                break;
        }

        return this;
    }

    @Override
    public void assertIt(List<List<Object>> data) {

        for (List<Object> list : data) {
            switch (((VerbsValidator) list.get(2)).toString()) {
                case "EQUALS":
                    resp.then().body(((String) list.get(0)), equalTo((String) list.get(1)));
                    break;

                case "KEY_PRESENTS":
                    resp.then().body(((String) list.get(0)), hasKey((String) list.get(1)));
                    break;

                case "HAS_ALL":
                    break;
            }
        }

    }

    @Override
    public void fileUpload(String key, String path, String mime) {
        reqSpec.multiPart(key, new File(path), mime);
    }

    @Override
    public String extractString(String path) {
        return resp.jsonPath().getString(path);
    }

    @Override
    public int extractInt(String path) {
        return resp.jsonPath().getInt(path);
    }

    @Override
    public List<?> extractList(String path) {
        return resp.jsonPath().getList(path);
    }

    @Override
    public Object extractIt(String path) {
        return resp.jsonPath().get(path);
    }

    @Override
    public String extractHeader(String headerName) {
        return resp.header(headerName);
    }

    @Override
    public String getResponseString() {
        return resp.asString();
    }

    @Override
    public void printResp() {
        resp.print();
    }

    public void multiPartString(String key, String input){
        reqSpec.multiPart(key,input);
    }
}
