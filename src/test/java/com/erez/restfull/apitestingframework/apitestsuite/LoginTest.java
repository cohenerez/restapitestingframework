package com.erez.restfull.apitestingframework.apitestsuite;
import com.relevantcodes.extentreports.LogStatus;
import java.lang.reflect.Method;
import com.erez.restfull.apitestingframework.verbs.VerbsValidator;
import com.erez.restfull.apitestingframework.baseapi.Authenticatior;
import com.erez.restfull.apitestingframework.listeners.ExtentTestManager;
import org.testng.annotations.Test;


public class LoginTest {



    @Test
    public void validLoginTest(Method method) {

        ExtentTestManager.startTest(method.getName(), "Description: Valid Login Scenario with username and password.");
        Authenticatior baseApi = new Authenticatior();
        baseApi.getLoginToken("admin", "password123");

        try {

            baseApi.assertIt(200);
            ExtentTestManager.getTest().log(LogStatus.INFO, "Asserting response code");

            baseApi.assertIt("token", null, VerbsValidator.NOT_EMPTY);
            ExtentTestManager.getTest().log(LogStatus.INFO, "Asserting response value not empty case");

            baseApi.assertIt("token", null, VerbsValidator.NOT_NULL);
            ExtentTestManager.getTest().log(LogStatus.INFO, "Asserting response value not null case");
        } catch (AssertionError e) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, "Assertion Failure: " + e.getMessage());

        }

    }

    @Test
    public void invalidLoginTest(Method method) {

        ExtentTestManager.startTest(method.getName(), "Description: InValid Login Scenario with username and password.");
        Authenticatior baseApi = new Authenticatior();
        baseApi.getLoginToken("dummy", "dummypassword123");

        try {

            baseApi.assertIt(200);
            ExtentTestManager.getTest().log(LogStatus.INFO, "Asserting response code");

            baseApi.assertIt("reason", "Bad credentials", VerbsValidator.EQUALS);
            ExtentTestManager.getTest().log(LogStatus.INFO, "Asserting response value == Bad credentials");

        } catch (AssertionError e) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, "Assertion Failure: " + e.getMessage());
        }

    }
}



