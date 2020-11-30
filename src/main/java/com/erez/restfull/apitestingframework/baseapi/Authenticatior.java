package com.erez.restfull.apitestingframework.baseapi;
import com.erez.restfull.apitestingframework.coreapitest.ApiTest;
import com.erez.restfull.apitestingframework.verbs.HttpVerbs;


public class Authenticatior   extends ApiTest{

    private void createToken(String userName, String passWord) {
        initBase("Host");
        init("/auth",HttpVerbs.POST);
        setHeader("Content-Type","application/json");
        setBody("{ \"username\" : \""+userName+"\", \"password\" : \""+passWord+"\"}");

    }

    public String getLoginToken(String userName, String passWord) {
        createToken(userName, passWord);
        String response = callIt();
        return response;
    }

}
